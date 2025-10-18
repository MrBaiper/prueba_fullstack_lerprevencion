const {Pool} = require('pg');
require('dotenv').config();

const pool = new Pool({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    database: process.env.DB_DATABASE,
    password: process.env.DB_PASS,
    port: process.env.DB_PORT
});

const probarConexion = async () => {
    try {
        const client = await pool.connect();
        console.log(`Conexión a la base de datos exitosa: ${process.env.DB_DATABASE}`);
    } catch (err) {
        console.error('Error de conexión a la base de datos:', err);
    }   
};

module.exports = {
    query: (text, params) => pool.query(text, params),
    pool,
    probarConexion
};