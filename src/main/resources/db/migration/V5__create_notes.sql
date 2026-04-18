-- V5__create_notes.sql
-- Creates note table with FK to person and advisor

CREATE TABLE note (
                      id          SERIAL PRIMARY KEY,
                      person_id   INT  NOT NULL,
                      created_by  INT,
                      content     TEXT NOT NULL,
                      created_at  TIMESTAMPTZ,

                      CONSTRAINT fk_note_person  FOREIGN KEY (person_id)  REFERENCES person(id),
                      CONSTRAINT fk_note_advisor FOREIGN KEY (created_by) REFERENCES advisor(id)
);