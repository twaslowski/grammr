ALTER TABLE app_user ADD COLUMN email VARCHAR;

ALTER TABLE app_user ADD COLUMN email_hash VARCHAR;

DROP INDEX IF EXISTS uq_app_user_email; -- on the previously renamed column username
CREATE UNIQUE INDEX uq_app_user_email_hash ON app_user(email_hash);