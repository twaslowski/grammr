CREATE TABLE IF NOT EXISTS deck
(
  id                BIGINT PRIMARY KEY,
  name              VARCHAR(255) NOT NULL,
  user_id           BIGINT       NOT NULL,
  created_timestamp TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_timestamp TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES "app_user" (id)
);

CREATE TABLE IF NOT EXISTS flashcard
(
  id                BIGINT PRIMARY KEY,
  question          VARCHAR(255) NOT NULL,
  answer            VARCHAR(255) NOT NULL,
  deck_id           BIGINT       NOT NULL,
  created_timestamp TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_timestamp TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (deck_id) REFERENCES deck (id)
);

CREATE SEQUENCE IF NOT EXISTS deck_id_seq INCREMENT 50 START 1;
CREATE SEQUENCE IF NOT EXISTS flashcard_id_seq INCREMENT 50 START 1;