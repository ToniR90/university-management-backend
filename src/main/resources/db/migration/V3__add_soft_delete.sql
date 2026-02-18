-- ADD new column "deleted_at" to all tables, which will be used for soft delete functionality
-- ADD new column "active" to all tables, which will be used to indicate whether a record is active or not

ALTER TABLE students
    ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE,
    ADD COLUMN deleted_at TIMESTAMPTZ;
