# prueba_fullstack_lerprevencion

Prueba técnica para empresa lerprevencion, cargo fullstack junior

## Requisitos previos

- Node.js 18+
- Java 17+
- PostgreSQL 14+
- Angular CLI (`npm install -g @angular/cli`)
- Git

---

## 1. Configuración de la base de datos

1. **Crea la base de datos en PostgreSQL:**

```sql
CREATE DATABASE prueba_fullstack;
```

2. **Crea la tabla `usuarios`:**

```sql
CREATE TABLE usuarios (
  id SERIAL PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  edad INTEGER NOT NULL
);
```

---

## 2. Backend Node.js + Express

1. Ve a la carpeta `backend`:

```bash
cd backend
```

2. Instala dependencias:

```bash
npm install
```

3. Configura la conexión a PostgreSQL en el archivo `.env`:

```
PGUSER=tu_usuario
PGPASSWORD=tu_contraseña
PGHOST=localhost
PGPORT=5432
PGDATABASE=prueba_fullstack
```

4. Inicia el servidor Node.js:

```bash
npm run dev
```

El backend Node.js expone el endpoint GET para consultar usuarios en:  
`http://localhost:3000/api/data`

---

## 3. Backend Java (CRUD)

1. Ve a la carpeta `crud_java`:

```bash
cd crud_java
```

2. Compila y ejecuta el servidor Java (ajusta según tu entorno):

```bash
javac -cp "lib/*" src/*.java -d bin
java -cp "bin;lib/*" Main
```

El backend Java expone los endpoints para crear, actualizar y eliminar usuarios en:  
`http://localhost:8080/api/data`

---

## 4. Frontend Angular

1. Ve a la carpeta `frontend`:

```bash
cd frontend
```

2. Instala dependencias:

```bash
npm install
```

3. Inicia la aplicación Angular:

```bash
ng serve
```

La interfaz estará disponible en:  
`http://localhost:4200`

---

## 5. Pruebas del microservicio

### Usando Postman

- **GET usuarios:**  
  `GET http://localhost:3000/api/data`

- **Crear usuario:**  
  `POST http://localhost:8080/api/data`  
  Body (JSON):
  ```json
  {
    "nombre": "Ejemplo",
    "email": "ejemplo@email.com",
    "edad": 30
  }
  ```

- **Actualizar usuario:**  
  `PUT http://localhost:8080/api/data/{id}`  
  Body (JSON):
  ```json
  {
    "nombre": "Nuevo Nombre",
    "email": "nuevo@email.com",
    "edad": 35
  }
  ```

- **Eliminar usuario:**  
  `DELETE http://localhost:8080/api/data?id={id}`

### Usando el frontend

- Accede a `http://localhost:4200` y utiliza la interfaz para crear, editar, eliminar y consultar usuarios.

---

## Notas

- Asegúrate de que los puertos no estén ocupados y que la base de datos esté corriendo.
- Si usas Docker, puedes crear un archivo `docker-compose.yml` para facilitar la configuración.
- Adjunta el script SQL de la tabla en el repositorio para facilitar la instalación.

---

## Estructura del proyecto

- `backend/` - Node.js + Express (GET usuarios)
- `crud_java/` - Java + PostgreSQL (CRUD usuarios)
- `frontend/` - Angular + Bootstrap (interfaz)

---