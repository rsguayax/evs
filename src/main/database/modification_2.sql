-- ALTER TABLE cap DROP COLUMN name;
ALTER TABLE cap ADD COLUMN name character varying(40);

-- ALTER TABLE cap DROP COLUMN ssid;
ALTER TABLE cap ADD COLUMN ssid character varying(40);

-- ALTER TABLE cap DROP COLUMN key;
ALTER TABLE cap ADD COLUMN key character varying(40);
