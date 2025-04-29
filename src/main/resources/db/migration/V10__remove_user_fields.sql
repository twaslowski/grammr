DROP TABLE "user_session";

ALTER TABLE "app_user" RENAME TO "user";

ALTER TABLE "user" DROP COLUMN email;
ALTER TABLE "user" DROP COLUMN email_hash;
ALTER TABLE "user" DROP COLUMN password;
ALTER TABLE "user" DROP COLUMN username;

ALTER TABLE "user" ADD COLUMN external_id VARCHAR;

-- Step 1: Drop the foreign key constraint in the dependent table
ALTER TABLE deck DROP CONSTRAINT deck_user_id_fkey;

-- Step 2: Alter the column type in the `user` table
ALTER TABLE "user" ALTER COLUMN id TYPE VARCHAR;

-- Step 3: Alter the column type in the dependent table
ALTER TABLE deck ALTER COLUMN user_id TYPE VARCHAR;

-- Step 4: Recreate the foreign key constraint
ALTER TABLE deck
  ADD CONSTRAINT deck_user_id_fkey FOREIGN KEY (user_id) REFERENCES "user" (id);