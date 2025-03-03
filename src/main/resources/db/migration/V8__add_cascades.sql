ALTER TABLE flashcard
  DROP CONSTRAINT IF EXISTS flashcard_deck_id_fkey;

ALTER TABLE flashcard
  ADD CONSTRAINT flashcard_deck_id_fkey
    FOREIGN KEY (deck_id) REFERENCES deck (id) ON DELETE CASCADE;

ALTER TABLE deck
  DROP CONSTRAINT IF EXISTS deck_user_id_fkey;

ALTER TABLE deck
  ADD CONSTRAINT deck_user_id_fkey
    FOREIGN KEY (user_id) REFERENCES "app_user" (id) ON DELETE CASCADE;