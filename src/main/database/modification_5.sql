ALTER TABLE student_type ADD CONSTRAINT styudent_type_value_unique UNIQUE (value);
ALTER TABLE matter DROP COLUMN academicperiod_id;
ALTER TABLE matter DROP COLUMN mode_id;