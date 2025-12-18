drop database if exists void_gate;
create database if not exists void_gate;
USE void_gate;
Create TABLE if not exists User(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    nombre varchar(255) NOT null ,
    email varchar (255) not null unique,
    password varchar (255) not null,
    monedas INT default 0,
    emailVerificado boolean default false,
    codigoVerificacion VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS Producto (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    precio INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Inventory (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    itemId INT NOT NULL,
    cantidad INT DEFAULT 1,
    FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE,
    FOREIGN KEY (itemId) REFERENCES Producto(id) ON DELETE CASCADE,
    UNIQUE (userId, itemId)
);
CREATE TABLE IF NOT EXISTS Evento (
                                      id VARCHAR(50) PRIMARY KEY,
                                      nombre VARCHAR(255) NOT NULL,
                                      fechaInicio VARCHAR(50),
                                      fechaFin VARCHAR(50),
                                      imageUrl VARCHAR(500),
                                      descripcion TEXT
);
INSERT INTO Evento (id, nombre, fechaInicio, fechaFin, imageUrl, descripcion)
VALUES (
           'ev-001',
           'Torneo DSA',
           '2024-06-01',
           '2024-06-03',
           'https://i.imgur.com/O50mtqt.png',
           'Competición de programación extrema para alumnos de la UPC.'
       );
CREATE TABLE IF NOT EXISTS EventoParticipacion (
                                                   id VARCHAR(50) PRIMARY KEY,
                                                   idUsuario VARCHAR(50),
                                                   idEvento VARCHAR(50)
);
