-- Table: email_notifications

-- DROP TABLE email_notifications;

CREATE TABLE email_notifications
(
  id bigint NOT NULL,
  user_id bigint NOT NULL,
  token character varying(36) NOT NULL,
  sent timestamp without time zone NOT NULL,
  read timestamp without time zone,
  evaluationevent_id bigint,
  CONSTRAINT email_notifications_pkey PRIMARY KEY (id),
  CONSTRAINT evaluationevent_id_fkey FOREIGN KEY (evaluationevent_id)
      REFERENCES evaluation_event (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
      REFERENCES evs_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT email_notifications_unique UNIQUE (user_id, evaluationevent_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE email_notifications
  OWNER TO postgres;

-- Index: fki_user_id_fkey

-- DROP INDEX fki_user_id_fkey;

CREATE INDEX fki_user_id_fkey
  ON email_notifications
  USING btree
  (user_id);
