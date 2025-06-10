-- Migrate user id to UUID instead of VARCHAR
ALTER TABLE deck
  DROP CONSTRAINT IF EXISTS deck_user_id_fkey;

-- 4e13d54: Nothing of this has been applied to dev/prod yet, so this change is safe
-- Remove legacy user ids that are not UUIDs
DELETE FROM "user"
  WHERE NOT (id ~* '^[0-9a-f]{8}\-[0-9a-f]{4}\-[0-9a-f]{4}\-[0-9a-f]{4}\-[0-9a-f]{12}$');

ALTER TABLE "user"
  ALTER COLUMN id TYPE UUID USING id::UUID;

ALTER TABLE deck
  ALTER COLUMN user_id TYPE UUID USING user_id::UUID;

ALTER TABLE deck
  ADD CONSTRAINT deck_user_id_fkey FOREIGN KEY (user_id) REFERENCES "user" (id);

-- Create new entities with correct types
CREATE TABLE IF NOT EXISTS chat
(
  id                BIGINT PRIMARY KEY,
  chat_id           UUID                     NOT NULL,
  owner             UUID                     NULL,
  created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (owner) REFERENCES "user" (id)
);

CREATE TABLE IF NOT EXISTS chat_message
(
  id                BIGINT PRIMARY KEY,
  content           TEXT,
  role              VARCHAR(16)              NOT NULL,
  chat_id           BIGINT                   NOT NULL,
  created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  FOREIGN KEY (chat_id) REFERENCES chat (id) ON DELETE CASCADE
);

CREATE SEQUENCE IF NOT EXISTS chat_id_seq INCREMENT 50 START 1;
CREATE SEQUENCE IF NOT EXISTS chat_message_id_seq INCREMENT 50 START 1;