ALTER TABLE flashcard ADD COLUMN
  token_pos VARCHAR(8);

ALTER TABLE flashcard ADD COLUMN
  paradigm_id UUID REFERENCES paradigm(id);