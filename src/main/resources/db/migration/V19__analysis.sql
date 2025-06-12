CREATE TABLE IF NOT EXISTS analysis
(
  id              BIGINT PRIMARY KEY,
  phrase          TEXT                     NOT NULL,
  source_language TEXT                     NOT NULL,
  analysis        JSON                     NOT NULL,
  created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at      TIMESTAMP WITH TIME ZONE NOT NULL
);

ALTER TABLE chat_message ADD COLUMN IF NOT EXISTS analysis_id BIGINT;

CREATE SEQUENCE IF NOT EXISTS analysis_id_seq START WITH 1 INCREMENT BY 50;