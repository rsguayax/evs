ALTER SEQUENCE hibernate_sequence RESTART WITH 500;

INSERT INTO role (id, code, name) VALUES(1, 'ADMIN', 'Administrador');
INSERT INTO role (id, code, name) VALUES(2, 'TEACHER', 'Profesor');
INSERT INTO role (id, code, name) VALUES(3, 'STUDENT', 'Estudiante');
INSERT INTO role (id, code, name) VALUES(4, 'STAFF', 'Personal UTPL');

INSERT INTO available_state (id, value) VALUES(1, 'AVAILABLE');
INSERT INTO available_state (id, value) VALUES(2, 'FULL');

INSERT INTO evs_user (id, enabled, password, username, firstname, lastname) VALUES(1, 1, '$2a$10$ZL5uhMfSvdOLgen0zmZl1.Vpa8SAUJWM6OypVWM1JVbP6cl8hZ2gS', 'admin', 'Admin', 'MS2S');
INSERT INTO evs_user (id, enabled, password, username, firstname, lastname) VALUES(2, 1, '$2a$10$pLZk6SxILRaZgpAlZS5MF.Az3NWVtuWksktfZ5qKBS.Qt1FogexBK', 'aperez', 'Alberto', 'Pérez López');
INSERT INTO evs_user (id, enabled, password, username, firstname, lastname) VALUES(3, 1, '$2a$10$pLZk6SxILRaZgpAlZS5MF.Az3NWVtuWksktfZ5qKBS.Qt1FogexBK', 'rjimenez', 'Rocío', 'Jimenez Alonso');
INSERT INTO evs_user (id, enabled, password, username, firstname, lastname) VALUES(4, 1, '$2a$10$pLZk6SxILRaZgpAlZS5MF.Az3NWVtuWksktfZ5qKBS.Qt1FogexBK', 'jgonzalez', 'Juan', 'González Muñoz');
INSERT INTO evs_user (id, enabled, password, username, firstname, lastname) VALUES(5, 1, '$2a$10$pLZk6SxILRaZgpAlZS5MF.Az3NWVtuWksktfZ5qKBS.Qt1FogexBK', 'anaranjo', 'Ana', 'Naranjo Megías');
INSERT INTO evs_user (id, enabled, password, username, firstname, lastname) VALUES(6, 1, '$2a$10$pLZk6SxILRaZgpAlZS5MF.Az3NWVtuWksktfZ5qKBS.Qt1FogexBK', 'lfernandez', 'Lucas', 'Fernández López');
INSERT INTO evs_user (id, enabled, password, username, firstname, lastname) VALUES(7, 1, '$2a$10$pLZk6SxILRaZgpAlZS5MF.Az3NWVtuWksktfZ5qKBS.Qt1FogexBK', 'falvarez', 'Francisco', 'Álvarez Peña');
INSERT INTO evs_user (id, enabled, password, username, firstname, lastname) VALUES(8, 1, '$2a$10$pLZk6SxILRaZgpAlZS5MF.Az3NWVtuWksktfZ5qKBS.Qt1FogexBK', 'salonso', 'Silvia', 'Alonso Cid');
INSERT INTO evs_user (id, enabled, password, username, firstname, lastname) VALUES(9, 1, '$2a$10$pLZk6SxILRaZgpAlZS5MF.Az3NWVtuWksktfZ5qKBS.Qt1FogexBK', 'lperez', 'Luis', 'Pérez López');

INSERT INTO user_role (user_id, role_id) VALUES(1, 1);
INSERT INTO user_role (user_id, role_id) VALUES(2, 1);
INSERT INTO user_role (user_id, role_id) VALUES(3, 1);
INSERT INTO user_role (user_id, role_id) VALUES(4, 2);
INSERT INTO user_role (user_id, role_id) VALUES(5, 2);
INSERT INTO user_role (user_id, role_id) VALUES(6, 3);
INSERT INTO user_role (user_id, role_id) VALUES(7, 3);
INSERT INTO user_role (user_id, role_id) VALUES(8, 4);
INSERT INTO user_role (user_id, role_id) VALUES(9, 4);

INSERT INTO matter (id, academiclevel, academicperiod, mode, name) VALUES (1, '1', '2015/2016', '1', 'Economía Industrial');
INSERT INTO matter (id, academiclevel, academicperiod, mode, name) VALUES (2, '1', '2015/2016', '1', 'Inteligencia Artificial');
INSERT INTO matter (id, academiclevel, academicperiod, mode, name) VALUES (3, '1', '2015/2016', '1', 'Mercadotecnia Estratégica');
INSERT INTO matter (id, academiclevel, academicperiod, mode, name) VALUES (4, '1', '2015/2016', '1', 'Economía y Población');
INSERT INTO matter (id, academiclevel, academicperiod, mode, name) VALUES (5, '1', '2015/2016', '1', 'Auditoría Informática');
INSERT INTO matter (id, academiclevel, academicperiod, mode, name) VALUES (6, '1', '2015/2016', '1', 'Administración de Proyectos Informáticos');
INSERT INTO matter (id, academiclevel, academicperiod, mode, name) VALUES (7, '1', '2015/2016', '1', 'Modelamiento de Datos');
INSERT INTO matter (id, academiclevel, academicperiod, mode, name) VALUES (8, '1', '2015/2016', '1', 'Teoría de Juegos');

INSERT INTO bank (id, createdate, currentnumber, externalid, iscomplete, name, questionnumber, state) VALUES (1, '2016-03-21 18:30:00', '10', 1, 1, 'Economía Industrial 1', 20, 'Activo');
INSERT INTO bank (id, createdate, currentnumber, externalid, iscomplete, name, questionnumber, state) VALUES (2, '2016-03-21 18:30:00', '10', 2, 1, 'Inteligencia Artificial 1', 20, 'Activo');
INSERT INTO bank (id, createdate, currentnumber, externalid, iscomplete, name, questionnumber, state) VALUES (3, '2016-03-21 18:30:00', '10', 3, 1, 'Mercadotecnia Estratégica 1', 20, 'Activo');
INSERT INTO bank (id, createdate, currentnumber, externalid, iscomplete, name, questionnumber, state) VALUES (4, '2016-03-21 18:30:00', '10', 4, 1, 'Economía y Población 1', 20, 'Activo');
INSERT INTO bank (id, createdate, currentnumber, externalid, iscomplete, name, questionnumber, state) VALUES (5, '2016-03-21 18:30:00', '10', 5, 1, 'Auditoría Informática', 20, 'Activo');
INSERT INTO bank (id, createdate, currentnumber, externalid, iscomplete, name, questionnumber, state) VALUES (6, '2016-03-21 18:30:00', '10', 6, 1, 'Administración de Proyectos Informáticos 1', 20, 'Activo');
INSERT INTO bank (id, createdate, currentnumber, externalid, iscomplete, name, questionnumber, state) VALUES (7, '2016-03-21 18:30:00', '10', 7, 1, 'Modelamiento de Datos 1', 20, 'Activo');
INSERT INTO bank (id, createdate, currentnumber, externalid, iscomplete, name, questionnumber, state) VALUES (8, '2016-03-21 18:30:00', '10', 8, 1, 'Teoría de Juegos 1', 20, 'Activo');

INSERT INTO matter_bank(matter_id, bank_id) VALUES (1, 1);
INSERT INTO matter_bank(matter_id, bank_id) VALUES (2, 2);
INSERT INTO matter_bank(matter_id, bank_id) VALUES (3, 3);
INSERT INTO matter_bank(matter_id, bank_id) VALUES (4, 4);
INSERT INTO matter_bank(matter_id, bank_id) VALUES (5, 5);
INSERT INTO matter_bank(matter_id, bank_id) VALUES (6, 6);
INSERT INTO matter_bank(matter_id, bank_id) VALUES (7, 7);
INSERT INTO matter_bank(matter_id, bank_id) VALUES (8, 8);
