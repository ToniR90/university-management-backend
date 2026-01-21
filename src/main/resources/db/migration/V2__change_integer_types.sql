-- Change SMALLINT to INTEGER to match JPA Integer type mapping
-- Affects: alumni_graduation_year, rgpd_signed_year

ALTER TABLE students
ALTER COLUMN alumni_graduation_year TYPE INTEGER;

ALTER TABLE students
ALTER COLUMN rgpd_signed_year TYPE INTEGER;