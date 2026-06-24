-- V8__make_email_nullable.sql
-- Makes email nullable in person table to allow persons without email

ALTER TABLE person ALTER COLUMN email DROP NOT NULL;