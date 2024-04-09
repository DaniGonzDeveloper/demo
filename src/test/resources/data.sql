-- authorities definition
USE "db";

-- Insertar datos
INSERT INTO authorities (role) VALUES ('ROLE_normal');
INSERT INTO users (name, last_name, email, phone_number, dni, street, password, sign_up_date)
VALUES ('test', 'test', 'test@gmail.com', '+34617928372', '78167751C', 'test', '$2a$10$wsiwDY5fB9FTXnezXnZ8veegaMM6v1Xfqkm3p5M5Mp5frivzXmBca', '2024-04-10');
INSERT INTO user_authorities (user_id, authorities_id) VALUES (1, 1);
