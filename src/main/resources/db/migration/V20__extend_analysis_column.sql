ALTER TABLE analysis
  ADD COLUMN analysis_id UUID NOT NULL default gen_random_uuid();

ALTER TABLE analysis
  RENAME COLUMN analysis TO analysed_tokens;

ALTER TABLE analysis
  RENAME COLUMN created_at TO created_timestamp;

ALTER TABLE analysis
  RENAME COLUMN updated_at TO updated_timestamp;

ALTER TABLE analysis
  ADD COLUMN IF NOT EXISTS target_language VARCHAR(2);

ALTER TABLE analysis
  ALTER COLUMN source_language TYPE VARCHAR(2);