DROP DATABASE IF EXISTS db_usuario;
CREATE DATABASE db_usuario;
USE db_usuario;

CREATE TABLE t_usuario
(
	id int not null primary key auto_increment,
    nombre varchar(50) not null,
    profesion varchar(50) not null,
    imagen longblob,
    ruta_imagen varchar(100)
);

INSERT INTO t_usuario(nombre, profesion) VALUES ('Cristian David Henao Hoyos', 'Ingeniero de Sistemas y Computación');
INSERT INTO t_usuario(nombre, profesion) VALUES ('María Camila Zapata', 'Estudiante');
INSERT INTO t_usuario(nombre, profesion) VALUES ('Miguel Angel Henao', 'Estudiante');
INSERT INTO t_usuario(nombre, profesion) VALUES ('Juan Camilo Pérez', 'Abogado');
INSERT INTO t_usuario(nombre, profesion) VALUES ('Pepe Andrés Marín', 'Estudiante de Derecho');
INSERT INTO t_usuario(nombre, profesion) VALUES ('Miguel Zapata', 'Estudiante de Derecho');