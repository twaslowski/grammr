ALTER TABLE user_session ALTER COLUMN id TYPE VARCHAR;

ALTER TABLE app_user RENAME COLUMN email TO username;