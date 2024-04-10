DROP SCHEMA IF EXISTS "db" CASCADE;

CREATE SCHEMA "db";
USE "db";

CREATE TABLE authorities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(50)
);

-- users definition
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50),
    email VARCHAR(150) NOT NULL,
    phone_number VARCHAR(15),
    dni VARCHAR(50),
    street VARCHAR(150),
    password VARCHAR(250) NOT NULL,
    sign_up_date DATE NOT NULL
);

-- user_authorities definition
CREATE TABLE user_authorities (
    user_id INT NOT NULL,
    authorities_id INT NOT NULL,
    PRIMARY KEY (user_id, authorities_id),
    FOREIGN KEY (authorities_id) REFERENCES authorities(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);