--------------------------------
--        EXTENSIONS          --
--------------------------------
CREATE EXTENSION unaccent; --Eliminar acentos en las consiltas


--------------------------------
--         INDEXES            --
--------------------------------
CREATE INDEX iuemail_index ON evs_user USING btree (email);
CREATE INDEX ifmtssession_index ON matter_test_student USING btree (session_id);
CREATE INDEX ifmtsse_index ON matter_test_student USING btree (studentevaluation_id);
CREATE INDEX ifeam_center ON evaluation_assignment_matter USING btree (center_id);
CREATE INDEX ifeam_evaluationeventmatter ON evaluation_assignment_matter USING btree (evaluationeventmatter_id);
CREATE INDEX ifeam_evaluationassignment ON evaluation_assignment_matter USING btree (evaluationassignment_id);
CREATE INDEX irole_code ON role USING btree (code);
CREATE INDEX ifurole_role ON user_role USING btree (role_id);
CREATE INDEX ifurole_user ON user_role USING btree (user_id);
CREATE INDEX ifea_user ON evaluation_assignment USING btree (user_id);
CREATE INDEX ifeemet_eem ON evaluation_event_matter_evaluation_type USING btree (evaluationeventmatter_id);
CREATE INDEX ifse_ea ON student_evaluation USING btree (evaluation_assignment_id);
CREATE INDEX ifse_crtb ON student_evaluation USING btree (classroom_time_block_id);
CREATE INDEX ifstsl_ctb ON student_test_schedule_log USING btree (classroom_time_block_id);
CREATE INDEX ifstsl_ea ON student_test_schedule_log USING btree (evaluation_assignment_id);
CREATE INDEX ifstsl_mts ON student_test_schedule_log USING btree (matter_test_student_id);


--------------------------------
--            ROLE            --
--------------------------------
INSERT INTO role (id, code, name) VALUES(1, 'ADMIN', 'Administrador');
INSERT INTO role (id, code, name) VALUES(2, 'TEACHER', 'Profesor');
INSERT INTO role (id, code, name) VALUES(3, 'STUDENT', 'Estudiante');
INSERT INTO role (id, code, name) VALUES(4, 'STAFF', 'Personal UTPL');


--------------------------------
--          USER ADMIN        --
--------------------------------
INSERT INTO evs_user (id, enabled, password, username, firstname, lastname) VALUES(1, 1, '$2a$10$ZL5uhMfSvdOLgen0zmZl1.Vpa8SAUJWM6OypVWM1JVbP6cl8hZ2gS', 'admin', 'Admin', 'MS2S');
INSERT INTO evs_user (id, enabled, identification, username, firstname, lastname, email) VALUES(2, 1, '74656177', '74656177', 'Francisco', 'Garrido Garrido', 'fgarrido@gmail.com');
INSERT INTO evs_user (id, enabled, identification, username, firstname, lastname, email) VALUES(3, 1, '97847120', '97847120', 'Laura', 'Ruíz Gómez', 'lruizgomez@gmail.com');
INSERT INTO evs_user (id, enabled, identification, username, firstname, lastname, email) VALUES(4, 1, '54113047', '54113047', 'Antonio', 'Pérez Alonso', 'aperezal@gmail.com');
INSERT INTO evs_user (id, enabled, identification, username, firstname, lastname, email) VALUES(5, 1, '10672100', '10672100', 'María', 'García Segovia', 'mgarciaseg@gmail.com');
INSERT INTO evs_user (id, enabled, identification, username, firstname, lastname, email) VALUES(6, 1, '46035874', '46035874', 'Jesús', 'Álvarez Jiménes', 'jalvarez@gmail.com');
INSERT INTO evs_user (id, enabled, identification, username, firstname, lastname, email) VALUES(7, 1, '24478036', '24478036', 'Victoria', 'Espigares Ruz', 'vespigares@gmail.com');
INSERT INTO user_role (user_id, role_id) VALUES(1, 1);
INSERT INTO user_role (user_id, role_id) VALUES(2, 3);
INSERT INTO user_role (user_id, role_id) VALUES(3, 3);
INSERT INTO user_role (user_id, role_id) VALUES(4, 3);
INSERT INTO user_role (user_id, role_id) VALUES(5, 3);
INSERT INTO user_role (user_id, role_id) VALUES(6, 3);
INSERT INTO user_role (user_id, role_id) VALUES(7, 3);


--------------------------------
--      AVAILABLE_STATE       --
--------------------------------
INSERT INTO available_state (id, value) VALUES(1, 'AVAILABLE');
INSERT INTO available_state (id, value) VALUES(2, 'FULL');


--------------------------------
--     EVALUATION_CENTER      --
--------------------------------
INSERT INTO evaluation_center (id, description, name, code) VALUES (1, '', 'AMBATO', 'AMB');
INSERT INTO evaluation_center (id, description, name, code) VALUES (2, '', 'CARIAMANGA', 'CAR');
INSERT INTO evaluation_center (id, description, name, code) VALUES (3, '', 'CUENCA', 'CUE');
INSERT INTO evaluation_center (id, description, name, code) VALUES (4, '', 'ESMERALDAS', 'ESM');
INSERT INTO evaluation_center (id, description, name, code) VALUES (5, '', 'GUAYAQUIL', 'GUA');
INSERT INTO evaluation_center (id, description, name, code) VALUES (6, '', 'GUAYAQUIL - CENTENARIO', 'GUA_C');
INSERT INTO evaluation_center (id, description, name, code) VALUES (7, '', 'IBARRA', 'IBA');
INSERT INTO evaluation_center (id, description, name, code) VALUES (8, '', 'LATACUNGA', 'LAT');
INSERT INTO evaluation_center (id, description, name, code) VALUES (9, '', 'LOJA', 'LOJ');
INSERT INTO evaluation_center (id, description, name, code) VALUES (10, '', 'MACAS', 'MAC');
INSERT INTO evaluation_center (id, description, name, code) VALUES (11, '', 'MACHALA', 'MLA');
INSERT INTO evaluation_center (id, description, name, code) VALUES (12, '', 'MANTA', 'MAN');
INSERT INTO evaluation_center (id, description, name, code) VALUES (13, '', 'PORTOVIEJO', 'POR');
INSERT INTO evaluation_center (id, description, name, code) VALUES (14, '', 'QUITO', 'QUI');
INSERT INTO evaluation_center (id, description, name, code) VALUES (15, '', 'QUITO-CARCELÉN', 'QUI_C');
INSERT INTO evaluation_center (id, description, name, code) VALUES (16, '', 'QUITO-SAN RAFAEL', 'QUI_S');
INSERT INTO evaluation_center (id, description, name, code) VALUES (17, '', 'QUITO-TUMBACO', 'QUI_T');
INSERT INTO evaluation_center (id, description, name, code) VALUES (18, '', 'QUITO-TURUBAMBA', 'QUI_TR');
INSERT INTO evaluation_center (id, description, name, code) VALUES (19, '', 'QUITO-VILLAFLORA', 'QUI_V');
INSERT INTO evaluation_center (id, description, name, code) VALUES (20, '', 'RIOBAMBA', 'RIO');
INSERT INTO evaluation_center (id, description, name, code) VALUES (21, '', 'SALINAS', 'SAL');
INSERT INTO evaluation_center (id, description, name, code) VALUES (22, '', 'SAMBORONDON', 'SAM');
INSERT INTO evaluation_center (id, description, name, code) VALUES (23, '', 'SANTO DOMINGO', 'SAN');
INSERT INTO evaluation_center (id, description, name, code) VALUES (24, '', 'TULCÁN', 'TUL');
INSERT INTO evaluation_center (id, description, name, code) VALUES (25, '', 'ZAMORA', 'ZAM');
INSERT INTO evaluation_center (id, description, name, code) VALUES (26, '', 'CAÑAR', 'CAN');
INSERT INTO evaluation_center (id, description, name, code) VALUES (27, '', 'EVALUACIÓN ONLINE', 'EVA_ON');


--------------------------------
--         CLASSROOM          --
--------------------------------
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (1, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 001', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (2, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 002', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (3, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 003', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (4, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 004', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (5, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 005', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (6, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 006', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (7, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 007', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (8, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 008', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (9, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 009', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (10, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 010', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (11, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 011', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (12, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 012', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (13, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 013', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (14, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 014', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (15, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 015', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (16, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 016', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (17, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 017', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (18, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 018', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (19, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 019', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (20, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 020', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (21, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 021', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (22, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 022', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (23, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 023', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (24, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 024', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (25, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 025', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (26, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 026', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (27, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 027', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (28, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 028', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (29, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 029', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (30, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 030', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (31, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 031', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (32, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 032', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (33, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 033', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (34, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 034', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (35, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 035', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (36, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 036', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (37, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 037', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (38, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 038', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (39, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 039', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (40, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 040', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (41, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 041', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (42, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 042', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (43, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 043', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (44, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 044', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (45, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 045', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (46, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 046', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (47, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 047', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (48, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 048', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (49, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 049', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (50, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 050', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (51, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 051', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (52, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 052', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (53, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 053', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (54, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 054', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (55, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 055', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (56, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 056', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (57, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 057', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (58, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 058', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (59, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 059', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (60, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 060', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (61, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 061', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (62, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 062', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (63, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 063', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (64, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 064', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (65, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 065', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (66, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 066', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (67, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 067', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (68, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 068', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (69, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 069', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (70, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 070', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (71, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 071', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (72, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 072', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (73, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 073', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (74, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 074', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (75, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 075', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (76, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 076', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (77, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 077', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (78, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 078', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (79, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 079', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (80, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 080', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (81, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 081', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (82, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 082', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (83, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 083', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (84, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 084', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (85, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 085', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (86, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 086', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (87, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 087', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (88, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 088', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (89, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 089', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (90, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 090', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (91, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 091', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (92, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 092', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (93, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 093', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (94, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 094', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (95, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 095', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (96, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 096', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (97, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 097', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (98, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 098', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (99, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 099', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (100, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 100', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (101, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 101', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (102, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 102', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (103, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 103', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (104, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 104', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (105, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 105', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (106, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 106', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (107, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 107', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (108, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 108', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (109, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 109', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (110, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 110', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (111, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 111', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (112, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 112', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (113, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 113', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (114, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 114', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (115, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 115', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (116, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 116', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (117, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 117', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (118, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 118', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (119, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 119', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (120, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 120', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (121, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 121', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (122, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 122', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (123, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 123', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (124, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 124', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (125, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 125', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (126, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 126', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (127, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 127', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (128, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 128', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (129, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 129', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (130, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 130', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (131, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 131', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (132, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 132', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (133, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 133', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (134, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 134', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (135, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 135', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (136, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 136', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (137, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 137', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (138, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 138', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (139, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 139', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (140, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 140', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (141, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 141', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (142, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 142', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (143, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 143', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (144, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 144', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (145, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 145', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (146, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 146', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (147, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 147', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (148, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 148', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (149, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 149', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (150, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 150', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (151, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 151', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (152, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 152', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (153, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 153', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (154, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 154', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (155, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 155', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (156, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 156', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (157, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 157', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (158, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 158', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (159, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 159', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (160, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 160', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (161, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 161', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (162, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 162', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (163, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 163', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (164, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 164', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (165, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 165', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (166, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 166', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (167, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 167', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (168, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 168', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (169, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 169', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (170, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 170', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (171, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 171', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (172, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 172', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (173, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 173', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (174, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 174', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (175, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 175', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (176, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 176', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (177, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 177', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (178, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 178', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (179, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 179', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (180, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 180', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (181, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 181', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (182, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 182', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (183, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 183', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (184, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 184', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (185, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 185', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (186, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 186', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (187, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 187', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (188, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 188', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (189, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 189', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (190, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 190', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (191, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 191', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (192, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 192', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (193, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 193', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (194, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 194', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (195, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 195', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (196, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 196', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (197, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 197', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (198, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 198', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (199, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 199', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (200, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 200', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (201, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 201', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (202, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 202', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (203, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 203', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (204, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 204', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (205, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 205', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (206, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 206', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (207, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 207', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (208, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 208', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (209, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 209', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (210, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 210', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (211, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 211', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (212, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 212', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (213, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 213', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (214, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 214', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (215, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 215', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (216, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 216', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (217, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 217', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (218, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 218', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (219, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 219', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (220, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 220', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (221, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 221', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (222, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 222', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (223, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 223', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (224, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 224', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (225, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 225', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (226, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 226', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (227, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 227', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (228, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 228', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (229, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 229', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (230, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 230', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (231, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 231', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (232, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 232', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (233, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 233', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (234, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 234', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (235, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 235', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (236, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 236', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (237, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 237', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (238, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 238', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (239, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 239', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (240, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 240', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (241, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 241', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (242, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 242', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (243, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 243', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (244, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 244', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (245, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 245', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (246, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 246', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (247, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 247', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (248, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 248', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (249, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 249', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (250, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 250', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (251, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 251', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (252, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 252', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (253, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 253', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (254, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 254', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (255, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 255', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (256, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 256', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (257, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 257', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (258, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 258', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (259, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 259', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (260, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 260', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (261, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 261', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (262, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 262', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (263, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 263', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (264, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 264', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (265, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 265', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (266, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 266', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (267, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 267', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (268, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 268', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (269, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 269', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (270, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 270', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (271, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 271', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (272, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 272', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (273, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 273', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (274, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 274', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (275, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 275', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (276, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 276', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (277, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 277', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (278, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 278', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (279, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 279', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (280, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 280', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (281, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 281', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (282, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 282', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (283, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 283', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (284, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 284', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (285, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 285', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (286, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 286', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (287, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 287', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (288, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 288', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (289, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 289', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (290, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 290', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (291, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 291', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (292, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 292', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (293, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 293', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (294, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 294', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (295, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 295', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (296, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 296', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (297, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 297', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (298, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 298', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (299, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 299', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (300, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 300', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (301, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 301', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (302, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 302', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (303, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 303', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (304, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 304', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (305, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 305', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (306, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 306', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (307, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 307', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (308, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 308', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (309, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 309', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (310, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 310', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (311, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 311', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (312, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 312', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (313, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 313', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (314, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 314', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (315, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 315', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (316, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 316', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (317, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 317', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (318, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 318', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (319, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 319', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (320, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 320', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (321, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 321', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (322, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 322', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (323, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 323', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (324, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 324', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (325, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 325', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (326, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 326', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (327, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 327', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (328, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 328', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (329, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 329', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (330, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 330', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (331, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 331', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (332, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 332', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (333, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 333', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (334, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 334', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (335, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 335', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (336, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 336', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (337, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 337', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (338, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 338', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (339, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 339', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (340, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 340', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (341, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 341', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (342, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 342', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (343, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 343', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (344, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 344', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (345, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 345', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (346, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 346', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (347, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 347', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (348, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 348', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (349, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 349', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (350, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 350', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (351, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 351', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (352, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 352', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (353, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 353', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (354, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 354', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (355, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 355', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (356, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 356', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (357, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 357', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (358, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 358', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (359, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 359', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (360, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 360', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (361, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 361', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (362, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 362', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (363, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 363', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (364, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 364', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (365, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 365', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (366, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 366', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (367, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 367', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (368, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 368', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (369, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 369', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (370, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 370', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (371, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 371', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (372, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 372', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (373, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 373', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (374, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 374', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (375, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 375', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (376, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 376', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (377, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 377', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (378, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 378', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (379, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 379', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (380, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 380', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (381, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 381', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (382, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 382', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (383, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 383', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (384, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 384', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (385, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 385', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (386, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 386', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (387, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 387', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (388, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 388', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (389, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 389', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (390, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 390', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (391, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 391', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (392, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 392', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (393, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 393', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (394, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 394', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (395, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 395', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (396, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 396', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (397, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 397', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (398, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 398', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (399, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 399', 35, 27);
INSERT INTO classroom (id, createby, createddate, lastmodifiedby, lastmodifieddate, available, location, name, seats, evaluationcenter_id) VALUES (400, 'admin', '2021-04-22 00:00:00', 'admin', '2021-04-22 00:00:00', TRUE, 'ONLINE', 'AULA ZOOM 400', 35, 27);


--------------------------------
--      EVALUATION_TYPE       --
--------------------------------
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (1, '1B', 'BIM1', 'Primer Bimestre MaD', 'Primer Bimestre MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (2, '2B', 'BIM2', 'Segundo Bimestre MaD', 'Segundo Bimestre MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (3, 'REC', 'RECU', 'Recuperación MaD', 'Recuperación MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (4, '1BCCE', 'E-BIM1', 'Primer Bimestre Competencias MaD', 'Primer Bimestre Competencias MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (5, '2BCCE', 'E-BIM2', 'Segundo Bimestre Competencias MaD', 'Segundo Bimestre Competencias MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (6, 'VG', 'VAL', 'Validación General MaD', 'Validación General MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (7, '1BCCEADA', '', 'Primer Bimestre Adaptada Competencias MaD', 'Primer Bimestre Adaptada Competencias MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (8, '2BCCEADA', '', 'Segundo Bimestre Adaptada Competencias MaD', 'Segundo Bimestre Adaptada Competencias MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (9, 'CACMP', '', 'Curso de Actualización de Modalidad Presencial', 'Curso de Actualización de Modalidad Presencial');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (10, 'RECCACMP', '', 'Recuperación Curso de Actualización de Modalidad Presencial', 'Recuperación Curso de Actualización de Modalidad Presencial');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (11, '1BAT', '', 'Primer Bimestre Atención Prioritaria MaD', 'Primer Bimestre Atención Prioritaria MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (12, '2BAT', '', 'Segundo Bimestre Atención Prioritaria MaD', 'Segundo Bimestre Atención Prioritaria MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (13, 'RECAT', '', 'Recuperación Atención Prioritaria MaD', 'Recuperación Atención Prioritaria MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (14, 'VGMP', '', 'Validación General Modalidad Presencial', 'Validación General Modalidad Presencial');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (15, 'CCEMP', '', 'Competencias Específicas de Modalidad Presencial', 'Competencias Específicas de Modalidad Presencial');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (16, 'VGIMP', '', 'Validación General de Inglés Modalidad Presencial', 'Validación General de Inglés Modalidad Presencial');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (17, 'IDO', '', 'Idoneidad de Inglés de MaD', 'Idoneidad de Inglés de MaD');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (18, 'ADMIMP', '', 'Admisiones universitarias - Modalidad Presencial', 'Admisiones universitarias - Modalidad Presencial');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (19, 'POS', '', 'Modulo de Posgrado', 'Modulo de Posgrado');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (20, 'POSREC', '', 'Recuperación Modulo de Posgrado', 'Recuperación Modulo de Posgrado');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (21, 'ECG', '', 'Examen Complexivo de Grado', 'Examen Complexivo de Grado');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (22, 'ECGG', '', 'Examen Complexivo de Grado - Gracia', 'Examen Complexivo de Grado - Gracia');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (23, 'ECP', '', 'Examen Complexivo de Posgrado', 'Examen Complexivo de Posgrado');
INSERT INTO evaluation_type (id, code, codews, name, description) VALUES (24, 'ECPG', '', 'Examen Complexivo de Posgrado - Gracia', 'Examen Complexivo de Posgrado - Gracia');


--------------------------------
--        USER_PROFILE        --
--------------------------------
INSERT INTO public.user_profile (id, description, name, code) VALUES (1, NULL, 'Administrador', 'administrator');
INSERT INTO public.user_profile (id, description, name, code) VALUES (2, NULL, 'Gestor de logística', 'logistics_manager');
INSERT INTO public.user_profile (id, description, name, code) VALUES (3, NULL, 'Soporte de centro universitario', 'university_center_supporter');
INSERT INTO public.user_profile (id, description, name, code) VALUES (4, NULL, 'Soporte de centro de evaluación', 'evaluation_center_supporter');
INSERT INTO public.user_profile (id, description, name, code) VALUES (5, NULL, 'Gestor de resultados', 'results_manager');
INSERT INTO public.user_profile (id, description, name, code) VALUES (6, NULL, 'Gestor de bancos', 'banks_manager');
INSERT INTO public.user_profile (id, description, name, code) VALUES (7, NULL, 'Alumno', 'student');
INSERT INTO public.user_profile (id,description,name,code) VALUES (8, NULL, 'Base', 'default');


--------------------------------
--          WEEK_DAY          --
--------------------------------
INSERT INTO week_day (id, code, name) VALUES (1, 'LUN', 'Lunes');
INSERT INTO week_day (id, code, name) VALUES (2, 'MAR', 'Martes');
INSERT INTO week_day (id, code, name) VALUES (3, 'MIE', 'Miércoles');
INSERT INTO week_day (id, code, name) VALUES (4, 'JUE', 'Jueves');
INSERT INTO week_day (id, code, name) VALUES (5, 'VIE', 'Viernes');
INSERT INTO week_day (id, code, name) VALUES (6, 'SAB', 'Sábado');
INSERT INTO week_day (id, code, name) VALUES (7, 'DOM', 'Domingo');


--------------------------------
--          TEST_TYPE         --
--------------------------------
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (1,'10 preguntas sobre 10 puntos',10,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (2,'10 preguntas sobre 16 puntos',10,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (3,'12 preguntas sobre 10 puntos',12,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (4,'13 preguntas sobre 10 puntos',13,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (5,'13 preguntas sobre 16 puntos',13,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (6,'13 preguntas sobre 20 puntos',13,20,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (7,'15 preguntas sobre 10 puntos',15,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (8,'15 preguntas sobre 16 puntos',15,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (9,'15 preguntas sobre 20 puntos',15,20,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (10,'16 preguntas sobre 10 puntos',16,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (11,'16 preguntas sobre 16 puntos',16,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (12,'16 preguntas sobre 20 puntos',16,20,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (13,'20 preguntas sobre 10 puntos',20,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (14,'20 preguntas sobre 16 puntos',20,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (15,'20 preguntas sobre 20 puntos',20,20,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (16,'22 preguntas sobre 10 puntos',22,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (17,'22 preguntas sobre 20 puntos',22,20,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (18,'23 preguntas sobre 10 puntos',23,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (19,'24 preguntas sobre 10 puntos',24,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (20,'24 preguntas sobre 16 puntos',24,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (21,'25 preguntas sobre 10 puntos',25,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (22,'25 preguntas sobre 16 puntos',25,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (23,'25 preguntas sobre 20 puntos',25,20,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (24,'27 preguntas sobre 10 puntos',27,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (25,'28 preguntas sobre 10 puntos',28,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (26,'30 preguntas sobre 10 puntos',30,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (27,'30 preguntas sobre 16 puntos',30,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (28,'30 preguntas sobre 20 puntos',30,20,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (29,'30 preguntas sobre 40 puntos',30,40,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (30,'32 preguntas sobre 10 puntos',32,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (31,'32 preguntas sobre 16 puntos',32,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (32,'32 preguntas sobre 20 puntos',32,20,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (33,'35 preguntas sobre 10 puntos',35,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (34,'35 preguntas sobre 16 puntos',35,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (35,'35 preguntas sobre 20 puntos',35,20,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (36,'39 preguntas sobre 40 puntos',39,40,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (37,'40 preguntas sobre 10 puntos',40,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (38,'40 preguntas sobre 16 puntos',40,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (39,'40 preguntas sobre 20 puntos',40,20,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (40,'40 preguntas sobre 40 puntos',40,40,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (41,'41 preguntas sobre 10 puntos',41,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (42,'42 preguntas sobre 10 puntos',42,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (43,'42 preguntas sobre 16 puntos',42,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (44,'45 preguntas sobre 10 puntos',45,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (45,'45 preguntas sobre 16 puntos',45,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (46,'50 preguntas sobre 10 puntos',50,10,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (47,'50 preguntas sobre 16 puntos',50,16,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (48,'50 preguntas sobre 20 puntos',50,20,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (49,'50 preguntas sobre 40 puntos',50,40,'(A*NM)/NP');
INSERT INTO test_type (id, name, maxnumquestion, maxrate,formula) VALUES (50,'50 preguntas sobre 50 puntos',50,50,'(A*NM)/NP');


--------------------------------
--          STUDENT_TYPE         --
--------------------------------
INSERT INTO student_type (id, value) VALUES (1, 'Regular');


--------------------------------
--      CORRECTION_RULE       --
--------------------------------
INSERT INTO correction_rule (id, mingrade, maxgrade, "type") VALUES (1, 0, 10, 'UTPL_RULE');


-------------------------------- -- ---
--      EVALUATION EVENT TYPES       --
-------------------------------- -- ---
INSERT INTO evaluation_event_types (id, modified, active, created, description, name) VALUES (1, '2021-01-01 00:00:00', true, '2021-01-01 00:00:00', 'Admisión Grado', 'Admisión Grado');
INSERT INTO evaluation_event_types (id, modified, active, created, description, name) VALUES (2, '2021-01-01 00:00:00', true, '2021-01-01 00:00:00', 'Complexivo Grado', 'Complexivo Grado');
INSERT INTO evaluation_event_types (id, modified, active, created, description, name) VALUES (3, '2021-01-01 00:00:00', true, '2021-01-01 00:00:00', 'Complexivo Postgrado', 'Complexivo Postgrado');
INSERT INTO evaluation_event_types (id, modified, active, created, description, name) VALUES (4, '2021-01-01 00:00:00', true, '2021-01-01 00:00:00', 'Grado Presencial', 'Grado Presencial');
INSERT INTO evaluation_event_types (id, modified, active, created, description, name) VALUES (5, '2021-01-01 00:00:00', true, '2021-01-01 00:00:00', 'Grado A distancia', 'Grado A distancia');
INSERT INTO evaluation_event_types (id, modified, active, created, description, name) VALUES (6, '2021-01-01 00:00:00', true, '2021-01-01 00:00:00', 'Validación Presencial', 'Validación Presencial');
INSERT INTO evaluation_event_types (id, modified, active, created, description, name) VALUES (7, '2021-01-01 00:00:00', true, '2021-01-01 00:00:00', 'Validación A distancia', 'Validación A distancia');


--------------------------------
--           SERVER           --
--------------------------------
INSERT INTO server (id, code, name, url) VALUES (1, 'EVL', 'EVL', 'https://evl.grammata.es');



