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


REINDEX DATABASE evs;


