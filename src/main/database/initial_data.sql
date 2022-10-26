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
--       ACADEMIC_LEVEL       --
--------------------------------
INSERT INTO academic_level (id, code, description, name) VALUES (1, 'POST01', 'Cuarto Nivel', 'POSTGRADO');
INSERT INTO academic_level (id, code, description, name) VALUES (2, 'PRE01', 'Tercer Nivel', 'PREGRADO');
INSERT INTO academic_level (id, code, description, name) VALUES (3, 'EC', 'EDUCACION CONTINUA', 'EDUCACION CONTINUA');
INSERT INTO academic_level (id, code, description, name) VALUES (4, 'GE', 'GRADO EXTERNO', 'GRADO EXTERNO');
INSERT INTO academic_level (id, code, description, name) VALUES (5, '00', 'NO DECLARADO', 'NO DECLARADO');
INSERT INTO academic_level (id, code, description, name) VALUES (6, 'UT', 'UNIDAD DE TITULACION', 'UNIDAD DE TITULACION');


--------------------------------
--      ACADEMIC_PERIOD       --
--------------------------------
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (1, 'PAC00214', 'Oct/2011 - May/2012.', '2012-05-31 00:00:00', 'aefa4b99-fbca-0040-e043-ac10360d0040', 'Oct/2011 - May/2012', '2011-10-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (2, 'POST2013-2', 'Periodo noviembre 2013 - mayo 2014 para Postgrados', '2014-05-19 00:00:00', 'e8516bd2-c0cf-00cc-e043-ac10360d00cc', 'Nov/2013 - May/2014', '2013-11-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (3, 'POST2012-2', 'Periodo noviembre 2012 - mayo 2013 para Postgrados', '2012-09-24 00:00:00', 'ca75fa96-73ea-0016-e043-ac10360d0016', 'Nov/2012 - May/2013', '2012-11-24 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (4, '00241', '.', '2015-12-31 00:00:00', '101c17b0-34db-007c-e053-ac10360c007c', 'May/2015-Oct/2015', '2015-03-03 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (5, 'POST2014', '', '2015-06-01 00:00:00', '044ecaec-af8f-007a-e053-ac10360d007a', 'Nov/2014 - May/2015', '2014-10-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (6, 'PA-2012-1', 'Configuración del periodo académico Abril - Agosto del 2012, realizado el 03-01-2012.', '2013-10-19 00:00:00', 'b5a3073b-8f2c-00e0-e043-ac10360c00e0', 'Abr/2012 - Ago/2012', '2012-01-04 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (7, 'POST2012-1', 'Periodo mayo - octubre 2012 para Postgrados ', '2012-06-30 00:00:00', 'bcbd504f-39e8-0026-e043-ac10360c0026', 'May/2012 - Oct/2012', '2012-05-26 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (8, '00249', 'Periodo académico, Abr/2016 - Ago/2016.', '2016-08-31 00:00:00', '20ff3375-a38c-00a4-e053-ac10360cfc12', 'Abr/2016 - Ago/2016', '2015-10-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (9, '00251', 'PE 1 -2016', '2016-03-31 00:00:00', '2587965f-9dc5-0048-e053-ac10360c69a8', 'PE 1 -2016', '2015-11-27 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (10, 'PE1-2015', 'PE 1 - 2015.', '2015-04-30 00:00:00', '07834fc5-386a-001e-e053-ac103633001e', 'PE 1 - 2015', '2014-11-10 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (11, '00253', 'Periodo', '2017-03-30 00:00:00', '2d975128-b5f0-00d6-e053-ac10360d90f4', 'Oct/2016 - Feb/2017', '2016-03-09 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (12, '218', 'Configuración del período académico octubre 2012-febrero 2013, realizado el día 9 de mayo del 2012', '2013-09-27 00:00:00', 'bf9bf395-d79d-00a2-e043-ac10360d00a2', 'Oct/2012 - Feb/2013', '2012-10-09 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (13, 'PAC00217', 'Mar/2012 - Sep/2012', '2012-09-30 00:00:00', 'cb528c0f-4feb-000a-e043-ac10360d000a', 'Mar/2012 - Sep/2012', '2012-03-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (14, 'PA-2013-1', 'Periodo Académico Abril 2013 - Agosto 2013. Modalidad Presencial y a Distancia', '2013-12-26 00:00:00', 'd2c751ee-88a6-0058-e043-ac10360d0058', 'Abr/2013 - Ago/2013', '2013-04-07 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (15, 'PAC00169', 'Ene/2008 - Mar/2008', '2008-03-30 00:00:00', '29e571bd-5b86-4de0-b4a7-280bfc9d158e', 'Ene/2008 - Mar/2008', '2008-01-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (16, 'PAC00177', 'Jul/2008 - Dic/2008', '2008-12-31 00:00:00', '0ebaa164-5d52-41b9-bb34-a051a386e69d', 'Jul/2008 - Dic/2008', '2008-07-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (17, 'PAC00232', 'Configuración del período Abr/2014 - Ago/2014, el 22 de Enero del 2014.', '2014-10-24 00:00:00', 'f0904fdc-f295-0048-e043-ac10360c0048', 'Abr/2014 - Ago/2014', '2014-01-27 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (18, 'PAC00234', 'Configuración del período Oct/2013 - Feb/2014, el 27 de Marzo del 2014', '2015-04-30 00:00:00', 'f59fe524-c534-007e-e043-ac10360c007e', 'Oct/2014 - Feb/2015', '2014-06-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (19, '00236', 'PE 2 - 2014', '2014-11-06 00:00:00', 'fdd8ed01-a76d-006a-e043-ac10360d006a', 'PE 2 - 2014', '2014-07-09 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (20, '00243', 'Periodo Academico, Oct/2015 - Feb/2016', '2016-03-31 00:00:00', '12b33259-97c8-00be-e053-ac10360d00be', 'Oct/2015 - Feb/2016', '2015-04-02 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (21, '00245', '', '2017-09-10 00:00:00', '1816a593-c90b-007a-e053-ac10360d007a', 'Oct/2015 - Ago/2017', '2015-06-10 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (22, '00247', 'Periodo Academico, Nov/2015 - May/2016.', '2016-05-31 00:00:00', '1f5740a5-54b3-0072-e053-ac10360db227', 'Nov/2015 - May/2016', '2015-09-10 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (23, '00248', 'Feb/2016 - Ene/2018.', '2018-04-10 00:00:00', '1f6cd862-2d2e-0056-e053-ac10360c9b3d', 'Feb/2016 - Ene/2018', '2015-09-10 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (24, '00250', 'Periodo Abr/2016 - Feb/2018.', '2018-02-28 00:00:00', '232a12d9-ca0c-0038-e053-ac103633a03a', 'Abr/2016 - Feb/2018', '2015-10-28 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (25, 'PAC00204', 'Nov/2011 - May/2012.', '2013-02-13 00:00:00', 'ace77393-669e-00f2-e043-ac10360d00f2', 'Nov/2011 - May/2012', '2011-09-15 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (26, 'POST2013-1', 'Periodo mayo - octubre 2013 para Postgrados', '2013-03-27 00:00:00', 'd8fcfda4-cf1d-005c-e043-ac10360d005c', 'May/2013 - Oct/2013', '2013-05-27 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (27, 'PAC00226', 'Configuración del período Oct/2013 - Feb/2014, el 21 de Mayo del 2013', '2014-02-28 00:00:00', 'dd3e9262-961a-00c2-e043-ac10360d00c2', 'Oct/2013 - Feb/2014', '2013-10-16 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (28, 'POST2014-1', 'Período May/2014 - Oct/2014 para Postgrados.', '2014-11-28 00:00:00', 'f72d4120-408b-0002-e043-ac10360d0002', 'May/2014 - Oct/2014', '2014-04-07 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (29, '00240', 'Abr/2015 - Ago/2015.', '2015-10-31 00:00:00', '08d494b3-9baf-0098-e053-ac10360d0098', 'Abr/2015 - Ago/2015', '2014-11-27 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (30, '00244', '', '2015-09-30 00:00:00', '16fd6e4b-4f38-0020-e053-ac1036330020', 'PE 2 - 2015', '2015-05-26 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (31, '00259', 'Periodos academico Oct/2017 - Feb/2018.', '2018-02-28 00:00:00', '4547a718-a8af-00c8-e053-ac10360cfC67', 'Oct/2017 - Feb/2018', '2017-05-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (32, '00257', 'Periodo Académico, Abr/2017 - Ago/2017.', '2017-10-30 00:00:00', '3ed90d28-853b-0002-e053-ac10360d4f56', 'Abr/2017 - Ago/2017', '2016-10-14 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (33, '00271', 'periodo postgrado: Octubre 2017 - agosto 2019..', '2019-09-30 00:00:00', '4a5505d7-14f0-00d0-e053-ac10360dd0ff', 'Oct/2017 - Ago/2019', '2017-03-10 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (34, '00272', 'Periodo PE 2 2017...', '2017-10-31 00:00:00', '4fe11488-a180-00dc-e053-ac10360c9642', 'PE 2 -2017', '2017-05-19 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (35, '00275', 'Periodo de Medicina Sep/2017 - Ago/2018..', '2018-08-31 00:00:00', '537e0505-7c67-003e-e053-ac10360d3527', 'Sep/2017 - Ago/2018', '2017-07-03 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (36, '00255', 'Periodo extraordinario PE 2 - 2016.', '2016-09-30 00:00:00', '32a95f99-86ca-00b4-e053-ac1036333061', 'PE 2 - 2016', '2016-05-13 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (37, '00261', 'Periodo académico Oct/2018 - Feb/2019', '2019-02-28 00:00:00', '455b9472-d201-008a-e053-ac10360cf9bb', 'Oct/2018 - Feb/2019', '2017-01-05 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (38, '00266', 'Periodo Extraordinario PE 1 -2017...', '2017-04-28 00:00:00', '465f6725-4d1a-0094-e053-ac10360c2b80', 'PE 1 -2017', '2017-01-18 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (39, '00262', 'Periodo Abr/2019 - Ago/2019.', '2019-08-31 00:00:00', '46c50734-470f-008a-e053-ac10360c415f', 'Abr/2019 - Ago/2019', '2017-01-24 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (40, '00263', 'Periodo Académico Oct/2019 - Feb/2020..', '2020-02-29 00:00:00', '46c50734-4710-008a-e053-ac10360c415f', 'Oct/2019 - Feb/2020', '2017-01-24 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (41, '00268', 'Periodo postgrado cortes intermedias - Ago/2016 - Ene/2017..', '2017-01-31 00:00:00', '46c50734-486d-008a-e053-ac10360c415f', 'Ago/2016 - Ene/2017', '2016-08-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (42, '00269', 'Periodo Postrado cortes intermedias - Feb/2017 – Jun/2017..', '2017-06-30 00:00:00', '46c50734-4886-008a-e053-ac10360c415f', 'Feb/2017 - Jun/2017', '2017-01-24 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (43, '00270', 'Periodo postgrado cortes intermedias Ago/2017 – Ene/2018..', '2018-01-31 00:00:00', '46c50734-4894-008a-e053-ac10360c415f', 'Ago/2017 - Ene/2018', '2017-01-24 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (44, '00258', 'Periodo académico, Abr/2017 - Feb/2019..', '2019-02-28 00:00:00', '3ed87a0c-068d-002a-e053-ac10360d379a', 'Abr/2017 - Feb/2019', '2016-10-14 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (45, '00260', 'Periodo académico Abr/2018 - Ago/2018.', '2018-08-31 00:00:00', '455a8ef6-4561-0058-e053-ac10360d8a72', 'Abr/2018 - Ago/2018', '2017-01-05 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (46, '00264', 'Periodo Abr/2017 – Feb/2020.', '2020-02-29 00:00:00', '46c452b4-e081-006a-e053-ac10360d076e', 'Abr/2017-Feb/2020', '2017-01-24 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (47, '00267', 'periodo de postgrado corte intermedia Feb/2016 - Jun/2016.', '2016-06-30 00:00:00', '46c32a78-d6ab-0094-e053-ac10360cc066', 'Feb/2016 - Jun/2016', '2016-02-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (48, '00273', 'Periodo académico para Postgrado..', '2020-03-01 00:00:00', '50e9753e-00ab-0000-e053-ac10360c11bc', 'Abr/2018- Feb/2020', '2017-06-01 00:00:00');
INSERT INTO academic_period (id, code, description, enddate, externalid, name, startdate) VALUES (49, '00254', 'Periodo Académico de Postgrado..', '2018-12-31 00:00:00', '2fa9721f-8042-00f2-e053-ac10360d242a', 'Oct/2016 - Ago/2018', '2016-04-04 00:00:00');


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


--------------------------------
--          CENTER            --
--------------------------------
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (1, NULL, 'Eugenio Espejo y Guayaquil junto a la Compañía de Infantería 8', 'ALAMOR', 'CENTRO', 'Asociado', 'ALA001', 'a7c2dd4b-a00d-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (2, NULL, 'Mariano Múñoz de Ayala Nº 103 y García Moreno', 'ALAUSÍ', 'CENTRO', 'Asociado', 'ALU001', 'a7c2dd4b-a00e-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (3, 1, 'Avenida Guaytambos y Manzanas esquina, SECTOR FICOA', 'AMBATO', 'CENTRO', 'Asociado Pais', 'AMB001', 'a7c2dd4b-9ff2-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (4, NULL, 'Av. Guayaquil y Alajhuela', 'ARENILLAS', 'CENTRO', 'Asociado', 'ARE001', 'a7c2dd4b-a00f-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (5, NULL, 'Ayacucho entre Veintimilla y Serrano', 'AZOGUES', 'CENTRO', 'Asociado Pais', 'AZO001', 'a7c2dd4b-a010-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (6, NULL, 's/n', 'Armada Nacional', 'CENTRO', 'Asociado', 'ARM001', 'acc5c398-7f0d-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (7, NULL, 'Ascázubi entre Bolívar y Montufar, Edificio Don Paco, segundo piso, junto al Ilustre Cantón Sucre', 'BAHÍA DE CARÁQUEZ', 'CENTRO', 'Asociado', 'BAH001', 'a7c2dd4b-a011-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (8, NULL, 'Ciudadela Machala, Callejón Jaime Roldos Aguilera', 'BALSAS', 'CENTRO', 'Asociado', 'BAL001', 'a7c2dd4b-a013-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (9, NULL, 'Vinces 724 y Baltazar Arauz', 'BALZAR', 'CENTRO', 'Asociado', 'BLZ001', 'a7c2dd4b-a012-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (10, NULL, 's/n', 'Babahoyo', 'CENTRO', 'Asociado', 'BAB001', 'acc5c398-7f0e-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (11, NULL, 'Universidad Católica Boliviana San Palbo Chiquitos', 'Bolivia (Centro)', 'CENTRO', 'Internacional', 'BOL001', 'a7c2dd4b-9ff0-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (12, NULL, 'Universidad Católica Boliviana San Palbo Chiquitos', 'Bolivia (Sede)', 'CENTRO', 'Internacional', 'BOL002', 'a7c2dd4b-9ff3-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (13, NULL, 's/n', 'Bomboiza', 'CENTRO', 'Asociado', 'BOM001', 'acc5c398-7f0f-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (14, NULL, '10 de Agosto entre Manabí y Abdón Calderón', 'CALCETA', 'CENTRO', 'Asociado', 'CAL001', 'a7c2dd4b-a014-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (15, 2, 'Extensión Universitaria Ciudadela Luis Alfonso Crespo Chiriboga', 'CARIAMANGA', 'CENTRO', 'Asociado', 'CAR001', 'a7c2dd4b-9ff4-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (16, NULL, 'Extensión Universitaria Ciudadela Luis Alfonso Crespo Chiriboga', 'CARIAMANGA EXTENSIÓN', 'EXTENSION', 'Asociado', 'CARE001', 'ac4db76d-d713-0090-e043-ac10360c0090');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (17, NULL, 'Domingo Celi y Naum Briones', 'CATACOCHA', 'CENTRO', 'Asociado', 'CAT001', 'a7c2dd4b-a017-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (18, NULL, 'Restauración S1-39 y Junín, Edificio Granda Cruz, segundo piso', 'CAYAMBE', 'CENTRO', 'Asociado', 'CAY001', 'a7c2dd4b-a018-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (19, 26, 'Ciudadela Nuevo Paraiso', 'CAÑAR', 'CENTRO', 'Asociado', 'CAN001', 'a7c2dd4b-a015-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (20, NULL, 'García Moreno y 10 de Agosto Primer Piso (colg. Santa Teresita)', 'CELICA', 'CENTRO', 'Asociado', 'CEL001', 'a7c2dd4b-a019-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (21, NULL, 'Bolívar 240 entre Colón y Pichincha edif. Vélez Frente a Pacifictel', 'CHONE', 'CENTRO', 'Asociado', 'CHO001', 'a7c2dd4b-a01b-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (22, NULL, 'Napo s/n y Cuenca', 'COCA', 'CENTRO', 'Asociado', 'COC001', 'a7c2dd4b-9ff5-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (23, 3, 'Gran Colombia 22167 y Unidad Nacional', 'CUENCA', 'CENTRO', 'Regional', 'CUE001', 'a7c2dd4b-9ff6-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (24, NULL, 's/n', 'Centro Modalidad Virtual', 'CENTRO', 'Asociado', 'VIR001', 'acc5c398-7f12-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (25, NULL, 's/n', 'Centro de Rehabilitacion - Guayaquil', 'CENTRO', 'Asociado', 'GYE002', 'acc5c398-7f10-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (26, NULL, 's/n', 'Centro de Rehabilitacion - Quito', 'CENTRO', 'Asociado', 'QTO002', 'acc5c398-7f11-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (27, NULL, 's/n', 'Chunchi', 'CENTRO', 'Asociado', 'CHU001', 'acc5c398-7f13-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (28, NULL, 'Bolívar entre Francisco Marcos y Malecón Planta Baja Iglesia Señor de los Milagros (Vicaría Episcopal Daule- Balzar)', 'DAULE', 'CENTRO', 'Asociado', 'DAU001', 'a7c2dd4b-a01c-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (29, NULL, 'Nicolás Lapentti 417 Ciudadela Pedro Menéndez Gilbert Mz 1 solar 5A Primer piso Alto oficina A4, referencia entrada Santuario del Divino Niño.', 'DURÁN', 'CENTRO', 'Asociado Pais', 'DUR001', 'a7c2dd4b-a01d-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (30, NULL, 'Esmeraldas y Abraham Herrera esquina casa de Gobierno Planta Baja', 'EL ANGEL', 'CENTRO', 'Asociado', 'ELA001', 'a7c2dd4b-a01e-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (31, NULL, 'calle 13 de enero y 13 de Junio', 'EL CHACO', 'CENTRO', 'Asociado', 'ECH001', 'a7c2dd4b-9fe2-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (32, NULL, 'Escuela Superior Militar de Aviación "Cosme Rennella" Chichipe, Av. 7ma. s/n', 'ESMA', 'CENTRO', 'Asociado', 'ESM001', 'a7c2dd4b-a01f-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (33, 4, 'Olmedo Nº 12-25 y 9 de octubre', 'ESMERALDAS', 'CENTRO', 'Asociado Pais', 'EMS001', 'a7c2dd4b-9ff7-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (34, NULL, 's/n', 'El Pangui', 'CENTRO', 'Asociado', 'EPG001', 'acc5c398-7f14-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (35, NULL, 's/n', 'El Tambo', 'CENTRO', 'Asociado', 'ELT001', 'acc5c398-7f15-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (36, NULL, 'Loja y Bolívar tercer piso Municipio', 'GONZANAMÁ', 'CENTRO', 'Asociado', 'GON001', 'a7c2dd4b-a020-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (37, 25, 'Parroquia Nuestra Señora de Guadalupe, Casa de Encuentro del Vicariato', 'GUADALUPE', 'CENTRO', 'Asociado', 'GUD001', 'a7c2dd4b-a021-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (38, NULL, '3 de Noviembre 11-23 entre Luis Ríos Rodríguez y Abelardo y Andrade', 'GUALACEO', 'CENTRO', 'Asociado', 'GUA001', 'a7c2dd4b-a022-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (39, NULL, 'Gonzalo Pesantes entre 24 de mayo y 16 de Agosto', 'GUALAQUIZA', 'CENTRO', 'Asociado', 'GUL001', 'a7c2dd4b-a023-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (40, NULL, 'Sucre y García Moreno Edificio del Sindicato de Choferes', 'GUARANDA', 'CENTRO', 'Asociado Pais', 'GUR001', 'a7c2dd4b-9ff8-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (41, 5, 'Av. Kennedy Nº333 entre la Av. San Jorge y calle F.', 'GUAYAQUIL', 'CENTRO', 'Regional', 'GYE001', 'a7c2dd4b-9ff9-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (42, 6, 'Rosa Borja de Icaza 108 A y El Oro', 'GUAYAQUIL - CENTENARIO', 'CENTRO', 'Regional', 'GYEC001', 'a7c2dd4b-a01a-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (43, NULL, 'Austria entre Carlos Vélez y 19 de Noviembre (Misión Franciscana)', 'GUAYSIMI', 'CENTRO', 'Asociado', 'GUY001', 'a7c2dd4b-a024-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (44, NULL, 'Avenida la República y Juan Montalvo, al frente de Chevi Plan "Clínica San Francisco"', 'HUAQUILLAS', 'CENTRO', 'Asociado', 'HUA001', 'a7c2dd4b-a025-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (45, NULL, 's/n', 'Huertas', 'CENTRO', 'Asociado', 'HUE001', 'acc5c398-7f16-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (46, 7, 'Av. Capitán Cristóbal de Troya 5 -211 y Luis Fernando Villamar (Intersección)', 'IBARRA', 'CENTRO', 'Asociado Pais', 'IBA001', 'a7c2dd4b-9ffa-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (47, NULL, 'Bolívar y Ricaurte esquina', 'JIPIJAPA', 'CENTRO', 'Asociado', 'JIP001', 'a7c2dd4b-a027-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (48, NULL, 'Misión Capuchina entre Estefanía Crespo y Jaime Roldos', 'JOYA DE LOS SACHAS', 'CENTRO', 'Asociado', 'JOY001', 'a7c2dd4b-a028-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (49, NULL, 's/n', 'Jama', 'CENTRO', 'Asociado', 'JAM001', 'acc5c398-7f17-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (50, NULL, 'Primero de Mayo entre 10 de Agosto y Avenida Simón Plata Torres frente al Parque Central', 'LA CONCORDIA', 'CENTRO', 'Asociado', 'LCO001', 'a7c2dd4b-a029-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (51, NULL, 'Las Fragatas y Antonio Gil', 'LA ISABELA', 'CENTRO', 'Asociado', 'ISA001', 'a7c2dd4b-a026-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (52, NULL, '10 de Agosto 507 y Andrés F. Córdova, Local 3', 'LA TRONCAL', 'CENTRO', 'Asociado', 'LAT001', 'a7c2dd4b-a02a-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (53, 8, 'Quito 11-176 y Marquez de Maenza(bajos Reg. Propiedad) San Agustín', 'LATACUNGA', 'CENTRO', 'Asociado Pais', 'LAG001', 'a7c2dd4b-9ffb-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (54, NULL, 'Parroquia Limones', 'LIMONES', 'CENTRO', 'Asociado', 'LIM001', 'a7c2dd4b-a02c-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (55, NULL, 'Calle Quito Vía al Pescado', 'LIMÓN INDANZA', 'CENTRO', 'Asociado', 'LII001', 'a7c2dd4b-a02b-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (56, 9, 'UTPL-Modalidad Abierta', 'LOJA', 'MATRIZ', 'Asociado Pais', 'LOJ001', 'a7c2dd4b-9ffc-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (57, NULL, 's/n', 'Las Lajas', 'CENTRO', 'Asociado', 'LLA001', 'acc5c398-7f18-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (58, NULL, 'Barrio Central, calle Manuel Enrique Rengel y Abdón Calderón', 'MACARÁ', 'CENTRO', 'Asociado', 'MAC001', 'a7c2dd4b-a02d-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (59, 10, '10 de Agosto y Soasti esquina', 'MACAS', 'CENTRO', 'Asociado Pais', 'MAA001', 'a7c2dd4b-9ffd-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (60, NULL, 'José Mejía 172 y Sucre', 'MACHACHI', 'CENTRO', 'Asociado', 'MCH001', 'a7c2dd4b-a02e-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (61, 11, 'Av. Bolívar Madero Vargas y Circunvalación Norte', 'MACHALA', 'CENTRO', 'Asociado Pais', 'MLA001', 'a7c2dd4b-9ffe-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (62, NULL, 'C/ LAGO CONSTANZA, 7 - Planta Baja, código postal 28017 Madrid', 'MADRID', 'CENTRO', 'Internacional', 'MAD007', 'a7c2dd4b-9fff-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (63, NULL, 'C/ LAGO CONSTANZA, 7 - Planta Baja, código postal 28017 Madrid', 'MADRID (BARCELONA)', 'CENTRO', 'Internacional', 'MAD006', 'a7c2dd4b-9fe8-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (64, NULL, 'C/ LAGO CONSTANZA, 7 - Planta Baja, código postal 28017 Madrid', 'MADRID (BILBAO)', 'CENTRO', 'Internacional', 'MAD008', 'aea5c4be-ca1e-00e2-e043-ac10360d00e2');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (65, NULL, 'C/ LAGO CONSTANZA, 7 - Planta Baja, código postal 28017 Madrid', 'MADRID (LONDRES)', 'CENTRO', 'Internacional', 'MAD005', 'a7c2dd4b-9fe9-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (66, NULL, 'C/ LAGO CONSTANZA, 7 - Planta Baja, código postal 28017 Madrid', 'MADRID (MURCIA)', 'CENTRO', 'Internacional', 'MAD001', 'a7c2dd4b-9fea-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (67, NULL, 'C/ LAGO CONSTANZA, 7 - Planta Baja, código postal 28017 Madrid', 'MADRID (PALMA DE MALLORCA)', 'CENTRO', 'Internacional', 'MAD004', 'aea5c4be-ca1f-00e2-e043-ac10360d00e2');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (68, NULL, 'C/ LAGO CONSTANZA, 7 - Planta Baja, código postal 28017 Madrid', 'MADRID (PAMPLONA)', 'CENTRO', 'Internacional', 'CMP001', 'ac4db76d-d715-0090-e043-ac10360c0090');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (69, NULL, 'C/ LAGO CONSTANZA, 7 - Planta Baja, código postal 28017 Madrid', 'MADRID (SUIZA)', 'CENTRO', 'Internacional', 'MAD002', 'a7c2dd4b-9feb-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (70, NULL, 'C/ LAGO CONSTANZA, 7 - Planta Baja, código postal 28017 Madrid', 'MADRID (VALENCIA)', 'CENTRO', 'Internacional', 'MAD003', 'a7c2dd4b-9fec-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (71, 12, 'Avenida Primera entre calle 23 y calle 24', 'MANTA', 'CENTRO', 'Asociado Pais', 'MAT001', 'a7c2dd4b-a000-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (72, NULL, 'Avenida Olmedo 100 y 9 de Octubre (Unidad Educativa San José)', 'MILAGRO', 'CENTRO', 'Asociado', 'CMI001', 'a7c2dd4b-a030-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (73, NULL, 's/n', 'Montalvo', 'CENTRO', 'Asociado', 'MON001', 'acc5c398-7f19-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (74, NULL, 'Cuenca y Domingo Comin', 'MÉNDEZ', 'CENTRO', 'Asociado', 'MEN001', 'a7c2dd4b-a02f-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (75, NULL, '86-58 Midland Parkway, Jamaica Estates, N.Y. 11432', 'NEW YORK', 'CENTRO', 'Internacional', 'NYK001', 'a7c2dd4b-a001-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (76, NULL, 'Av. Colombia 151 y La Ronda', 'NUEVA LOJA (LA)', 'CENTRO', 'Asociado Pais', 'NUL001', 'a7c2dd4b-a002-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (77, 7, 'Ciudadela Imbaya, Parroquia Eclesiastica San Vicente Ferrer', 'OTAVALO', 'CENTRO', 'Asociado', 'OTA001', 'a7c2dd4b-a031-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (78, NULL, '27 de Marzo frente al Estadio', 'PACTO', 'CENTRO', 'Asociado', 'PAC001', 'a7c2dd4b-a032-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (79, 11, 'Juan Montalvo y Aurelio Prieto frente a la ferretería La Económica', 'PASAJE', 'CENTRO', 'Asociado', 'PAS001', 'a7c2dd4b-a033-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (80, NULL, 'Avenida siglo XX y Rodríguez Parra', 'PAUTE', 'CENTRO', 'Asociado', 'PAU001', 'a7c2dd4b-a034-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (81, NULL, 'Avenida Plaza entre Guaranda y tulcán', 'PEDERNALES', 'CENTRO', 'Asociado', 'PED001', 'a7c2dd4b-a035-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (82, NULL, 'Av. 29 de Junio junto a la casa Parroquial', 'PEDRO VICENTE MALDONADO', 'CENTRO', 'Asociado', 'PVM001', 'a7c2dd4b-a036-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (83, NULL, 'Avenida 8 de Noviembre diagonal al Coliseo cerrado José Gallardo Moscoso', 'PIÑAS', 'CENTRO', 'Asociado', 'PIS001', 'a7c2dd4b-a037-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (84, 13, 'Ilustre Municipio de Portovelo', 'PORTOVELO', 'CENTRO', 'Asociado', 'POR001', 'a7c2dd4b-a038-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (85, NULL, 'Córdova entre Rocafuerte y Espejo', 'PORTOVIEJO', 'CENTRO', 'Asociado Pais', 'POV001', 'a7c2dd4b-a003-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (86, NULL, 'Ilustre Municipio de Pucará', 'PUCARÁ', 'CENTRO', 'Asociado', 'PUC001', 'a7c2dd4b-9fcf-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (87, NULL, 'Guayaquil entre Cotopaxi y Tungurahua', 'PUYO', 'CENTRO', 'Asociado Pais', 'PUY001', 'a7c2dd4b-a004-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (88, NULL, 's/n', 'Pindal', 'CENTRO', 'Asociado', 'PIN001', 'acc5c398-7f1a-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (89, NULL, 'Av. Quito, Ciudadela Nuevo Quevedo, manzana 5, solar 1, frente a la Universidad Estatal de Quevedo', 'QUEVEDO', 'CENTRO', 'Asociado Pais', 'QUE001', 'a7c2dd4b-a005-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (90, NULL, 'Av.3 de julio Nº 328 y Jimmy Anchico', 'QUININDÉ', 'CENTRO', 'Asociado', 'QUI001', 'a7c2dd4b-9fd0-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (91, 14, 'Av. 6 de Diciembre y Alpallana', 'QUITO', 'CENTRO', 'Regional', 'QTO001', 'a7c2dd4b-a006-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (92, 15, 'Av. Diego Vásquez de Cepeda y Mariano Paredes esquina (diagonal a colegio Einstein)', 'QUITO-CARCELÉN', 'CENTRO', 'Regional', 'CQC001', 'a7c2dd4b-a016-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (93, 16, 'Av. General Rumiñahui 369 y 6ta. Transversal o Isla Genovesa', 'QUITO-SAN RAFAEL', 'CENTRO', 'Regional', 'QTOSR001', 'a7c2dd4b-9fd4-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (94, 17, 'Avda. Interoceánica y Eloy Alfaro. Centro Comercial La Granja', 'QUITO-TUMBACO', 'CENTRO', 'Regional', 'TUM001', 'a7c2dd4b-9fe0-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (95, 18, 'Cusubamba lote 20 y Teniente Hugo Ortiz, al costado Registro Civil', 'QUITO-TURUBAMBA', 'CENTRO', 'Regional', 'TUR001', 'a7c2dd4b-9fe1-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (96, 19, 'Calle Chambo S8-507 y Calle Cerro Hermoso tras el Supermercado Santa María de la Villaflora', 'QUITO-VILLAFLORA', 'CENTRO', 'Regional', 'QTOV001', 'a7c2dd4b-9fe3-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (97, 20, 'Ciudadela los Álamos II - Calle Juan Chiriboga y Av. Canónigo Ramos', 'RIOBAMBA', 'CENTRO', 'Asociado Pais', 'RIO001', 'a7c2dd4b-a007-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (98, NULL, 'Vía Aurelia 773, 00165 Roma', 'ROMA', 'CENTRO', 'Internacional', 'ROM001', 'a7c2dd4b-a008-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (99, NULL, 'Vía Aurelia 773, 00165 Roma', 'ROMA (GÉNOVA)', 'CENTRO', 'Internacional', 'ROM002', 'a7c2dd4b-9fed-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (100, NULL, 'Vía Aurelia 773, 00165 Roma', 'ROMA (MILÁN)', 'CENTRO', 'Internacional', 'ROM003', 'a7c2dd4b-9fee-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (101, NULL, 'Vía Aurelia 773, 00165 Roma', 'ROMA (PARIS)', 'CENTRO', 'Internacional', 'ROM', 'a7c2dd4b-9fef-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (102, 21, 'Sector Puerto Rico Avenida Segunda y Calle 27A. Edificio Romarpro Primer Piso.', 'SALINAS', 'CENTRO', 'Asociado', 'SLI', 'a7c2dd4b-9fd1-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (103, 22, 'Km. 2,5 via a Samborondon Edif. Samborondon Plaza 1er piso Oficina 114', 'SAMBORONDON', 'CENTRO', 'Asociado', 'SAM', 'a7c2dd4b-9ff1-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (104, NULL, 'Segundo Matapuncho y Washington', 'SAN CRISTÓBAL', 'CENTRO', 'Asociado', 'SCR', 'a7c2dd4b-9fd2-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (105, NULL, 'Bolívar y García Moreno cerca de la Iglesia Matriz', 'SAN GABRIEL', 'CENTRO', 'Asociado', 'SGA', 'a7c2dd4b-9fd3-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (106, NULL, 'Gustavo Bedón y 22 de Marzo', 'SAN LORENZO', 'CENTRO', 'Asociado', 'SLO', 'a7c2dd4b-9fd5-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (107, NULL, 'Av. 17 de Julio diagonal al cuerpo de Bomberos', 'SAN MIGUEL DE LOS BANCOS', 'CENTRO', 'Asociado', 'SMB', 'a7c2dd4b-9fd6-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (108, NULL, 'Fragata e Isla Floriana, Barrio el EDEN diagonal a Interfol', 'SANTA CRUZ', 'CENTRO', 'Asociado', 'STC', 'a7c2dd4b-9fd7-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (109, NULL, 'Manabí y Fidel Rosales (Colegio Mensajero de la Paz) Junto al Mercado Central', 'SANTA ISABEL', 'CENTRO', 'Asociado', 'SIS', 'a7c2dd4b-9fd8-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (110, NULL, 'Sucre sin N° y Avenida Quito', 'SANTA ROSA', 'CENTRO', 'Asociado', 'STR', 'a7c2dd4b-9fd9-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (111, 23, 'Urbanización Pedro Moreira, Calle Principal Isabela / Avenida Chone Km 2', 'SANTO DOMINGO', 'CENTRO', 'Asociado Pais', 'STD', 'a7c2dd4b-a009-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (112, 21, 'Santuario Nacional María Blanca Estrella de la Mar Olón, Av. Principal entre Olón y Montañita', 'SANTUARIO DE OLÓN', 'CENTRO', 'Asociado', 'STO', 'a7c2dd4b-9fda-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (113, NULL, 'Avenida El Oro frente al Parque Central, segundo piso', 'SARAGURO', 'CENTRO', 'Asociado', 'SAR', 'a7c2dd4b-9fdb-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (114, NULL, 'Avenida Azuay y 10 de Agosto Municipio primer piso', 'SEVILLA DE ORO', 'CENTRO', 'Asociado', 'SDO', 'a7c2dd4b-9fdc-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (115, NULL, 'Avenida 7 de Agosto y Shuaras', 'SHUSHUFINDI', 'CENTRO', 'Asociado', 'SHU', 'a7c2dd4b-9fdd-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (116, NULL, '3 de Noviembre y Domingo Comín', 'SUCÚA', 'CENTRO', 'Asociado', 'SUC', 'a7c2dd4b-9fde-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (117, NULL, 's/n', 'Salcedo', 'CENTRO', 'Asociado', 'SAL', 'acc5c398-7f1b-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (118, NULL, 's/n', 'Sangolquí', 'CENTRO', 'Asociado', 'SAN', 'acc5c398-7f1c-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (119, NULL, 's/n', 'Sig Sig', 'CENTRO', 'Asociado', 'SIG', 'acc5c398-7f1d-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (120, NULL, 'Centro de Capacitación de la Fundación Cochasquí Urbanizaciòn Amistad y Progreso Panamericana (Frente al Colegio Tabacundo)', 'TABACUNDO', 'CENTRO', 'Asociado', 'TAB', 'a7c2dd4b-9fdf-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (121, NULL, 'Barrio Central calle Abdón Calderón y Juan León Mera esquina', 'TENA', 'CENTRO', 'Asociado Pais', 'TEN', 'a7c2dd4b-a00a-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (122, 24, 'Rafael Arellano y Junín Condominios del Banco de la Vivienda Ofc. 202', 'TULCÁN', 'CENTRO', 'Asociado Pais', 'TUL', 'a7c2dd4b-a00b-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (123, NULL, 'Bolívar y Urdaneta Edificio la Cascada Biblioteca Municipal', 'VINCES', 'CENTRO', 'Asociado', 'VIN', 'a7c2dd4b-9fe4-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (124, 25, 'Zamora 02-22 entre Armando Arias y Luis Bastidas', 'YANZATZA', 'CENTRO', 'Asociado', 'YAN', 'a7c2dd4b-9fe5-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (125, NULL, 's/n', 'Yacuambi', 'CENTRO', 'Asociado', 'TAC', 'acc5c398-7f1e-0024-e043-ac10360d0024');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (126, 25, 'Avenida del Ejército junto al Colegio 12 de Febrero', 'ZAMORA', 'CENTRO', 'Asociado Pais', 'ZAM', 'a7c2dd4b-a00c-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (127, NULL, 'Avenida del Ejército junto al Colegio 12 de Febrero', 'ZAMORA EXTENSIÓN', 'EXTENSION', 'Asociado Pais', 'ZAME', 'ac4db76d-d714-0090-e043-ac10360c0090');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (128, NULL, 'Sucre y Sexmo', 'ZARUMA', 'CENTRO', 'Asociado', 'ZAR', 'a7c2dd4b-9fe6-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (129, NULL, '12 de febrero y Francisco de Orellana', 'ZUMBA', 'CENTRO', 'Asociado', 'ZUM', 'a7c2dd4b-9fe7-004e-e043-ac10360d004e');
INSERT INTO center (id, evaluationcenter_id, address, name, stype, type, code, externalid) VALUES (130, NULL, 's/n', 'Zapotillo', 'CENTRO', 'Asociado', 'ZAP', 'acc5c398-7f1f-0024-e043-ac10360d0024');


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
--            MODE            --
--------------------------------
INSERT INTO mode (id, code, createdate, description, name, deleted) VALUES (1, 'DISTANCIA', '2010-07-13 00:00:00', 'Modalidad de Estudios a Distancia', 'Distancia', false);
INSERT INTO mode (id, code, createdate, description, name, deleted) VALUES (2, 'SEMIPRESENCIAL', '2015-07-28 00:00:00', 'Modalidad de estudios semipresencial', 'Semipresencial', false);
INSERT INTO mode (id, code, createdate, description, name, deleted) VALUES (3, 'PRESENCIAL', '2010-07-13 00:00:00', 'Modalidad de Estudios Presencial', 'Presencial', false);


--------------------------------
--            ROLE            --
--------------------------------
INSERT INTO role (id, code, name) VALUES(1, 'ADMIN', 'Administrador');
INSERT INTO role (id, code, name) VALUES(2, 'TEACHER', 'Profesor');
INSERT INTO role (id, code, name) VALUES(3, 'STUDENT', 'Estudiante');
INSERT INTO role (id, code, name) VALUES(4, 'STAFF', 'Personal UTPL');


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



------------------------------
--      GLOBAL CONFIGS      --
------------------------------
INSERT INTO global_configs(id, createdate, description, name, type, value) VALUES (1, '2021-03-31 23:27:37', 'Reglas de inscripciones', 'rules_enrolled', 'JSON', '[{"rule":"1","placeAvailiblesRule":true,"noteRule":true,"priority":1,"status":"SUITABLE","asignedPlace":true},{"rule":"2","placeAvailiblesRule":true,"noteRule":false,"priority":1,"status":"ZERO_COURSE","asignedPlace":true},{"rule":"3","placeAvailiblesRule":false,"noteRule":true,"priority":1,"status":"UNKNOW","asignedPlace":false},{"rule":"4","placeAvailiblesRule":false,"noteRule":false,"priority":1,"status":"UNSUITABLE","asignedPlace":false},{"rule":"5","placeAvailiblesRule":true,"noteRule":true,"priority":2,"status":"SUITABLE","asignedPlace":true},{"rule":"6","placeAvailiblesRule":true,"noteRule":false,"priority":2,"status":"ZERO_COURSE","asignedPlace":true},{"rule":"7","placeAvailiblesRule":false,"noteRule":false,"priority":2,"status":"UNSUITABLE","asignedPlace":false},{"rule":"8","placeAvailiblesRule":false,"noteRule":true,"priority":2,"status":"UNKNOW","asignedPlace":false}]');


ALTER SEQUENCE hibernate_sequence RESTART WITH 295;