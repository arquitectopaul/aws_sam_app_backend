
CREATE TABLE categoria (
    id SERIAL PRIMARY KEY,
    nombregrupo VARCHAR(255) NOT NULL,
    descripgrupo VARCHAR(255) NOT NULL
);

CREATE TABLE articulo (
    id SERIAL PRIMARY KEY,
    nombrearticulo VARCHAR(255) NOT NULL,
    descriparticulo VARCHAR(255) NOT NULL
);

CREATE TABLE custodio (
    id SERIAL PRIMARY KEY,
    arearesponsable VARCHAR(255) NOT NULL,
    nombresyapellidos VARCHAR(255) NOT NULL,
    correo VARCHAR(100) NULL,
    nombreusuario VARCHAR(255) NULL
);

CREATE TABLE proveedor (
    id SERIAL PRIMARY KEY,
    custodioId INTEGER NOT NULL,
    razonsocial VARCHAR(255) NOT NULL,
    ruc VARCHAR(18) NOT NULL,
    direccionfiscal VARCHAR(255) NOT NULL,
    contacto VARCHAR(255) NOT NULL,
    telefono VARCHAR(255) NOT NULL,
    correo VARCHAR(255) NOT NULL,
    FOREIGN KEY (custodioId) REFERENCES custodio(id)
);

CREATE TABLE tipo (
    id SERIAL PRIMARY KEY,
    nombretipo VARCHAR(255) NOT NULL,
    descriptipo VARCHAR(255) NOT NULL
);

CREATE TABLE dependencia (
    id SERIAL PRIMARY KEY,
    nombredependencia VARCHAR(255) NOT NULL,
    descripdependencia VARCHAR(255) NOT NULL
);

CREATE TABLE atributo (
    id SERIAL PRIMARY KEY,
    custodioId INTEGER NOT NULL,
    articuloId INTEGER NOT NULL,
    tipoId INTEGER NOT NULL,
    categoriaId INTEGER NOT NULL,
    FOREIGN KEY (custodioId) REFERENCES custodio(id),
    FOREIGN KEY (articuloId) REFERENCES articulo(id),
    FOREIGN KEY (tipoId) REFERENCES tipo(id),
    FOREIGN KEY (categoriaId) REFERENCES categoria(id)
);

CREATE TABLE atributos (
    id SERIAL PRIMARY KEY,
    atributoid INTEGER NULL,
    nombreatributo VARCHAR(255) NOT NULL,
    FOREIGN KEY (atributoid) REFERENCES atributo(id)
);

CREATE TABLE activo (
    id SERIAL PRIMARY KEY,
    custodioId INTEGER NOT NULL,
    articuloId INTEGER NOT NULL,
    tipoId INTEGER NOT NULL,
    categoriaId INTEGER NOT NULL,
    proveedorid INTEGER NULL,
    codinventario VARCHAR(50) NULL,
    modelo VARCHAR(255) NULL,
    marca VARCHAR(255) NULL,
    nroserie VARCHAR(255) NULL,
    fechaingreso DATE NOT NULL,
    fechaingresostr VARCHAR(20) NULL,
    moneda VARCHAR(10) NOT NULL,
    importe NUMERIC NOT NULL,
    descripcion VARCHAR(255) NULL,
    FOREIGN KEY (custodioId) REFERENCES custodio(id),
    FOREIGN KEY (articuloId) REFERENCES articulo(id),
    FOREIGN KEY (tipoId) REFERENCES tipo(id),
    FOREIGN KEY (categoriaId) REFERENCES categoria(id)
);

CREATE TABLE especificaciones (
    id SERIAL PRIMARY KEY,
    especificacionid INTEGER NULL,
    nombreatributo VARCHAR(255) NOT NULL,
    descripcionatributo VARCHAR(255) NULL,
    FOREIGN KEY (especificacionid) REFERENCES activo(id)
);

CREATE TABLE parametros (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255) NOT NULL
);

CREATE TABLE marca (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255) NOT NULL
);
