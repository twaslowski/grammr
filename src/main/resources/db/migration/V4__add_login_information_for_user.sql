DROP TABLE IF EXISTS "user";
DROP SEQUENCE IF EXISTS user_id_seq;

CREATE TABLE IF NOT EXISTS app_user
(
  id                BIGINT PRIMARY KEY,
  email             VARCHAR(255) NOT NULL,
  password          VARCHAR(255) NOT NULL,
  created_timestamp TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_timestamp TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_session
(
  id               BIGINT PRIMARY KEY,
  user_id          BIGINT       NOT NULL,
  session_token    VARCHAR(255) NOT NULL,
  created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_accessed_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  expires_at       TIMESTAMP    NOT NULL,
  FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_app_user_email ON app_user (email);
CREATE SEQUENCE IF NOT EXISTS user_id_seq INCREMENT 50 START 1;
CREATE SEQUENCE IF NOT EXISTS user_session_id_seq INCREMENT 50 START 1;