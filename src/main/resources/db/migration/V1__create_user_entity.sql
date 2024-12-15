CREATE TABLE IF NOT EXISTS "user"
(
  id                BIGINT PRIMARY KEY,
  chat_id           BIGINT,
  language_spoken   VARCHAR(2),
  language_learned  VARCHAR(2),
  created_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE IF NOT EXISTS user_id_seq INCREMENT 50 START 1;