const express = require('express');
const cors = require('cors');
require('dotenv').config();

const {probarConexion} = require('./db');
probarConexion();

const app = express();
app.use(cors());
app.use(express.json());

const usuariosRouter = require('./routes/usuarios');
app.use('/api/data', usuariosRouter);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Servidor On en: ${PORT}`);
});