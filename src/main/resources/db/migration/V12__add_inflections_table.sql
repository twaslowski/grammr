CREATE TABLE paradigm
(
  id             UUID PRIMARY KEY,
  lemma          TEXT                     NOT NULL,
  part_of_speech VARCHAR(8)               NOT NULL,
  language_code  VARCHAR(2)               NOT NULL,
  inflections    JSONB                    NOT NULL,
  created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uq_language_lemma_pos
  ON paradigm (language_code, lemma, part_of_speech);