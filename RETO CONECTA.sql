drop database examendb;
create database examendb;
USE examendb;
-- Tabla UnidadDidactica
CREATE TABLE unidadDidactica (
    id INT PRIMARY KEY AUTO_INCREMENT,
    acronimo VARCHAR(20),
    titulo VARCHAR(100),
    evaluacion VARCHAR(50),
    descripcion VARCHAR(100)
);

-- Tabla Enunciado
CREATE TABLE enunciado (
    id INT PRIMARY KEY AUTO_INCREMENT,
    descripcion VARCHAR(100),
    nivel ENUM('ALTA', 'MEDIA', 'BAJA'),
    disponible BOOLEAN DEFAULT TRUE,
    ruta VARCHAR(255)
);

CREATE TABLE uniEnu (
    id_uni INT,
    id_enu INT,
    PRIMARY KEY (id_uni, id_enu),
    FOREIGN KEY (id_uni) REFERENCES unidadDidactica(id),
    FOREIGN KEY (id_enu) REFERENCES enunciado(id)
);

-- -------------------
-- DATOS PREVIOS
-- -------------------

-- Unidades didácticas
INSERT INTO unidadDidactica (id, acronimo, titulo, evaluacion, descripcion) VALUES
(1, 'BD1', 'Introducción a Bases de Datos', 'Examen + Prácticas', 'Fundamentos de bases de datos relacionales.'),
(2, 'PRG1', 'Programación I', 'Examen final', 'Conceptos básicos de programación estructurada.'),
(3, 'WEB1', 'Desarrollo Web Básico', 'Proyecto web', 'Introducción al desarrollo frontend con HTML, CSS y JS.');

-- Enunciados
INSERT INTO enunciado (id, descripcion, nivel, disponible, ruta) VALUES
(1, 'Crear una base de datos con tablas y relaciones.', 'alta', TRUE, 'src/docs/Document2.pdf'),
(2, 'Escribir un programa que calcule la suma de los primeros 100 números.', 'baja', TRUE, 'src/docs/Práctica1-1.pdf'),
(3, 'Diseñar una página web con un formulario de contacto.', 'media', TRUE, 'src/docs/Práctica1.pdf'),
(4, 'Optimizar una consulta SQL con índices.', 'alta', FALSE, 'src/docs/fallosComponentes.pdf'),
(5, 'Implementar un bucle para calcular factorial.', 'media', TRUE, 'src/docs/Document2.pdf');

INSERT INTO unienu (id_uni, id_enu) VALUES
(1, 1),
(1, 4),
(2, 2), 
(2, 5),
(3, 3);