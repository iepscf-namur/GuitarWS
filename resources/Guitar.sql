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
    Salt BLOB NULL,
    PRIMARY KEY (login)
);

CREATE TABLE IF NOT EXISTS Guitar.Catalog (
   	idSong INT NOT NULL,
	artistName VARCHAR(64) NOT NULL,
	songTitle VARCHAR(255) NOT NULL,
	PRIMARY KEY (idSong)
);

GRANT ALL PRIVILEGES ON guitar.* to guituser@'%' IDENTIFIED BY 'guituser';
GRANT ALL PRIVILEGES ON guitar.* to guituser@'localhost' IDENTIFIED BY 'guituser';