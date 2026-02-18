-- Override unique constraint for test database
-- Hibernate ddl-auto=create-drop creates a simple UNIQUE on dni
-- We need a partial unique index that only applies to active students
ALTER TABLE students DROP CONSTRAINT IF EXISTS students_dni_key;
DROP INDEX IF EXISTS students_dni_active_unique;
CREATE UNIQUE INDEX students_dni_active_unique ON students (dni) WHERE active = true;