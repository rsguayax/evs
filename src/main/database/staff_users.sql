INSERT INTO evs_user (id, enabled, password, username, firstname, lastname, email, externalid, identification) VALUES (1, 1, NULL, 'dfcastillo3', 'DIEGO FERNANDO', 'CASTILLO DUARTE', 'dfcastillo3@utpl.edu.ec', '1104054497', NULL);
INSERT INTO user_user_profile (user_id, user_profile_id) VALUES (1, 1); -- "Administrador"
INSERT INTO user_role (user_id, role_id) VALUES (1, 4);

INSERT INTO evs_user (id, enabled, password, username, firstname, lastname, email, externalid, identification) VALUES (2, 1, NULL, 'kprueda', 'kprueda', 'kprueda', 'kprueda@utpl.edu.ec', '1', NULL);
INSERT INTO user_user_profile (user_id, user_profile_id) VALUES (2, 2); -- "Gestor de logística"
INSERT INTO user_role (user_id, role_id) VALUES (2, 4);

INSERT INTO evs_user (id, enabled, password, username, firstname, lastname, email, externalid, identification) VALUES (3, 1, NULL, 'ypjimenez', 'ypjimenez', 'ypjimenez', 'ypjimenez@utpl.edu.ec', '1', NULL);
INSERT INTO user_user_profile (user_id, user_profile_id) VALUES (3, 3); -- "Soporte de centro universitario"
INSERT INTO user_role (user_id, role_id) VALUES (3, 4);

INSERT INTO evs_user (id, enabled, password, username, firstname, lastname, email, externalid, identification) VALUES (4, 1, NULL, 'wdquezada', 'wdquezada', 'wdquezada', 'wdquezada@utpl.edu.ec', '1', NULL);
INSERT INTO user_user_profile (user_id, user_profile_id) VALUES (4, 4); -- "Soporte de centro de evaluación"
INSERT INTO user_role (user_id, role_id) VALUES (4, 4);

INSERT INTO evs_user (id, enabled, password, username, firstname, lastname, email, externalid, identification) VALUES (5, 1, NULL, 'spaute', 'spaute', 'spaute', 'spaute@utpl.edu.ec', '1', NULL);
INSERT INTO user_user_profile (user_id, user_profile_id) VALUES (5, 5); -- "Gestor de resultados"
INSERT INTO user_role (user_id, role_id) VALUES (5, 4);

INSERT INTO evs_user (id, enabled, password, username, firstname, lastname, email, externalid, identification) VALUES (6, 1, NULL, 'ggordonez', 'ggordonez', 'ggordonez', 'ggordonez@utpl.edu.ec', '1', NULL);
INSERT INTO user_user_profile (user_id, user_profile_id) VALUES (6, 6); -- "Gestor de bancos"
INSERT INTO user_role (user_id, role_id) VALUES (6, 4);

INSERT INTO evs_user (id, enabled, password, username, firstname, lastname, email, externalid, identification) VALUES (7, 1, NULL, 'admin', 'admin', 'admin', 'lmroldan@grammata.es', '1', NULL);
