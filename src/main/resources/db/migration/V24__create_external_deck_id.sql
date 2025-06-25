ALTER TABLE deck
  ADD COLUMN IF NOT EXISTS deck_id UUID NOT NULL DEFAULT gen_random_uuid();

ALTER TABLE deck
  RENAME COLUMN user_id TO owner;