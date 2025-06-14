ALTER TABLE chat_message
  DROP COLUMN analysis_id;

ALTER TABLE chat_message
  ADD COLUMN IF NOT EXISTS analysis_id UUID;