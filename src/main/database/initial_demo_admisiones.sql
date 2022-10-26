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


-----------------------------
--           CAP           --
-----------------------------
INSERT INTO cap (id, key, name, serialnumber, ssid, evaluationcenter_id, server_id) VALUES (1, '15698752', 'CAP LOJA 001', '69522010', 'localhost', 9, 1);
INSERT INTO cap (id, key, name, serialnumber, ssid, evaluationcenter_id, server_id) VALUES (2, '34003258', 'CAP LOJA 002', '72036547', 'localhost', 9, 1);
INSERT INTO cap (id, key, name, serialnumber, ssid, evaluationcenter_id, server_id) VALUES (3, '75632147', 'CAP LOJA 003', '31258796', 'localhost', 9, 1);


-----------------------------
--           NET           --
-----------------------------
INSERT INTO net (id, code, name, password, evaluationcenter_id) VALUES (1, 'RED-LOJA-001', 'RED LOJA 001', '87412369', 9);


