-- Override unique constraint for test database
-- Ensures DNI uniqueness only for active persons
ALTER TABLE person DROP CONSTRAINT IF EXISTS person_dni_key;
DROP INDEX IF EXISTS person_dni_active_unique;
CREATE UNIQUE INDEX person_dni_active_unique ON person (dni) WHERE active = true;