ALTER TABLE flashcard
  ADD COLUMN IF NOT EXISTS flashcard_id UUID NOT NULL DEFAULT gen_random_uuid(),
  ADD COLUMN IF NOT EXISTS sync_id UUID;