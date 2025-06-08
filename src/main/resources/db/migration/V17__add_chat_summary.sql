ALTER TABLE chat
  ADD COLUMN IF NOT EXISTS summary TEXT;

AlTER TABLE chat_message
  ADD COLUMN IF NOT EXISTS message_id UUID NOT NULL DEFAULT gen_random_uuid();