CREATE TABLE IF NOT EXISTS chat
(
  id                BIGINT PRIMARY KEY,
  chat_id           UUID                     NOT NULL,
  owner             VARCHAR                  NOT NULL,
  created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (owner) REFERENCES "user" (id)
);

CREATE TABLE IF NOT EXISTS chat_message (
  id BIGINT PRIMARY KEY,
  content TEXT,
  role VARCHAR(16) NOT NULL,
  chat_id BIGINT NOT NULL,
  created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (chat_id) REFERENCES chat (id)
);

CREATE SEQUENCE IF NOT EXISTS chat_id_seq INCREMENT 50 START 1;
CREATE SEQUENCE IF NOT EXISTS chat_message_id_seq INCREMENT 50 START 1;