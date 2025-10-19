import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleHttpServer {

    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/usuarios", (exchange -> {
            String method = exchange.getRequestMethod();
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equalsIgnoreCase(method)) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            try {
                switch (method) {
                    case "GET" -> {
                        List<Usuario> usuarios = UsuarioController.getUsuarios();
                        String response = convertirUsuariosAJson(usuarios);
                        sendResponse(exchange, 200, response);
                    }

                    case "POST" -> {
                        String body;
                        try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
                            body = reader.lines().collect(Collectors.joining("\n"));
                        }

                        Usuario nuevo = parseUsuarioDesdeJson(body);
                        boolean ok = UsuarioController.addUsuario(nuevo.getNombre(), nuevo.getCorreo(), nuevo.getEdad());

                        if (ok)
                            sendResponse(exchange, 201, "{\"message\":\"Usuario creado correctamente\"}");
                        else
                            sendResponse(exchange, 500, "{\"error\":\"No se pudo crear el usuario\"}");
                    }

                    case "PUT" -> {
                        String path = exchange.getRequestURI().getPath();
                        String[] parts = path.split("/");
                        if (parts.length < 3) {
                            sendResponse(exchange, 400, "{\"error\":\"ID no proporcionado\"}");
                            return;
                        }
                        int id = Integer.parseInt(parts[2]);

                        String body;
                        try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
                            body = reader.lines().collect(Collectors.joining("\n"));
                        }

                        Usuario actualizado = parseUsuarioDesdeJson(body);
                        boolean ok = UsuarioController.updateUsuario(
                                id, actualizado.getNombre(), actualizado.getCorreo(), actualizado.getEdad()
                        );

                        if (ok)
                            sendResponse(exchange, 200, "{\"message\":\"Usuario actualizado\"}");
                        else
                            sendResponse(exchange, 404, "{\"error\":\"Usuario no encontrado\"}");
                    }

                    case "DELETE" -> {
                        String query = exchange.getRequestURI().getQuery();
                        if (query == null || !query.contains("id=")) {
                            sendResponse(exchange, 400, "{\"error\":\"ID no proporcionado\"}");
                            return;
                        }
                        int id = Integer.parseInt(query.replace("id=", ""));
                        boolean ok = UsuarioController.deleteUsuario(id);

                        if (ok)
                            sendResponse(exchange, 200, "{\"message\":\"Usuario eliminado\"}");
                        else
                            sendResponse(exchange, 404, "{\"error\":\"Usuario no encontrado\"}");
                    }

                    default -> sendResponse(exchange, 405, "{\"error\":\"Método no permitido\"}");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "{\"error\":\"Error interno del servidor\"}");
            }
        }));

        System.out.println("✅ Servidor iniciado en http://localhost:8080");
        server.start();
    }

    private static void sendResponse(com.sun.net.httpserver.HttpExchange exchange, int code, String response)
            throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    //Conversion manual a JSON
    private static String convertirUsuariosAJson(List<Usuario> usuarios) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            sb.append("{")
              .append("\"id\":").append(u.getId()).append(",")
              .append("\"nombre\":\"").append(escapeJson(u.getNombre())).append("\",")
              .append("\"email\":\"").append(escapeJson(u.getCorreo())).append("\",")
              .append("\"edad\":").append(u.getEdad())
              .append("}");
            if (i < usuarios.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    //Parser JSON simple
    private static Usuario parseUsuarioDesdeJson(String json) {
        Usuario u = new Usuario();
        json = json.replaceAll("[{}\"]", "");
        String[] pares = json.split(",");
        for (String p : pares) {
            String[] kv = p.split(":");
            if (kv.length < 2) continue;
            String key = kv[0].trim();
            String value = kv[1].trim();
            switch (key) {
                case "nombre" -> u.setNombre(value);
                case "email", "correo" -> u.setCorreo(value);
                case "edad" -> u.setEdad(Integer.parseInt(value));
            }
        }
        return u;
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
