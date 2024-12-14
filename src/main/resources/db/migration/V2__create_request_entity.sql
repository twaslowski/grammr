CREATE TABLE request
(
  id                BIGINT PRIMARY KEY,
  user_id           BIGINT       NOT NULL,
  status            VARCHAR(255) NOT NULL,
  audio_file_path   VARCHAR(255),
  created_timestamp TIMESTAMP    NOT NULL,
  updated_timestamp TIMESTAMP    NOT NULL,
  completion_tokens INTEGER,
  prompt_tokens     INTEGER
);

CREATE SEQUENCE IF NOT EXISTS request_id_seq INCREMENT 50 START 1;