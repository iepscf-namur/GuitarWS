CREATE TABLE IF NOT EXISTS Guitar.Roles (
   	id INT NOT NULL,
    name VARCHAR(64) NOT NULL,
    description TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Guitar.Users (
    login VARCHAR(64) NOT NULL,
    userName VARCHAR(64) NOT NULL,
    idRoleUser INT NOT NULL,
    password BLOB NULL,
    PRIMARY KEY (login)
);

CREATE TABLE IF NOT EXISTS Guitar.Catalog (
   	idSong INT NOT NULL AUTO_INCREMENT,
	artistName VARCHAR(64) NOT NULL,
	songTitle VARCHAR(255) NOT NULL,
	PRIMARY KEY (idSong)
);

CREATE TABLE IF NOT EXISTS Guitar.Songs (
	id INT NOT NULL AUTO_INCREMENT,
	idCatalogSong INT NOT NULL,
	song BLOB NULL,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Guitar.Setup (
	id INT NOT NULL AUTO_INCREMENT,
	idCatalogSong INT NOT NULL,
	duration INT NOT NULL,
	fontSize INT NOT NULL,
	PRIMARY KEY (id)
);

GRANT ALL PRIVILEGES ON guitar.* to guituser@'%' IDENTIFIED BY 'guituser';
GRANT ALL PRIVILEGES ON guitar.* to guituser@'localhost' IDENTIFIED BY 'guituser';