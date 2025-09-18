ALTER TABLE flashcard
  DROP COLUMN IF EXISTS sync_id;

ALTER TABLE flashcard
  DROP COLUMN IF EXISTS type; -- can be inferred from paradigm