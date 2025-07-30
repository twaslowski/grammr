ALTER TABLE flashcard
  RENAME COLUMN question TO front;

ALTER TABLE flashcard
  RENAME COLUMN answer TO back;

CREATE UNIQUE INDEX IF NOT EXISTS idx_question ON flashcard (front, deck_id);