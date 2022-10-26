-- ALTER TABLE evaluation_event_configuration DROP COLUMN dailystudentsandschedulesloadstartdate;
ALTER TABLE evaluation_event_configuration ADD COLUMN dailystudentsandschedulesloadstartdate timestamp(6) without time zone;

-- ALTER TABLE evaluation_event_configuration DROP COLUMN dailystudentsandschedulesloadenddate;
ALTER TABLE evaluation_event_configuration ADD COLUMN dailystudentsandschedulesloadenddate timestamp(6) without time zone;

-- ALTER TABLE evaluation_event_configuration DROP COLUMN dailystudentsandschedulesloadtime;
ALTER TABLE evaluation_event_configuration ADD COLUMN dailystudentsandschedulesloadtime timestamp(6) without time zone;


-- ALTER TABLE evaluation_event_configuration DROP COLUMN dailyevaluationschedulesmailingstartdate;
ALTER TABLE evaluation_event_configuration ADD COLUMN dailyevaluationschedulesmailingstartdate timestamp(6) without time zone;

-- ALTER TABLE evaluation_event_configuration DROP COLUMN dailyevaluationschedulesmailingenddate;
ALTER TABLE evaluation_event_configuration ADD COLUMN dailyevaluationschedulesmailingenddate timestamp(6) without time zone;

-- ALTER TABLE evaluation_event_configuration DROP COLUMN dailyevaluationschedulesmailingtime;
ALTER TABLE evaluation_event_configuration ADD COLUMN dailyevaluationschedulesmailingtime timestamp(6) without time zone;
