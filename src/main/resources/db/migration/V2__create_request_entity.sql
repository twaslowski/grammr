CREATE TABLE request
(
  id                SERIAL PRIMARY KEY,
  user_id           BIGINT       NOT NULL,
  status            VARCHAR(255) NOT NULL,
  created_at        TIMESTAMP    NOT NULL,
  updated_at        TIMESTAMP    NOT NULL,
  completion_tokens INTEGER,
  prompt_tokens     INTEGER
)