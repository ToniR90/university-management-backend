-- V6__create_session.sql
-- Creates session, advisors_in_session, collaborators_in_session and assistants tables

CREATE TABLE session (
                         id              SERIAL PRIMARY KEY,
                         title           VARCHAR(255) NOT NULL,
                         description     TEXT,
                         motivation      TEXT,
                         session_type    VARCHAR(50)  NOT NULL,
                         session_origin  VARCHAR(50)  NOT NULL,
                         all_welcome     BOOLEAN,
                         start_datetime  TIMESTAMPTZ,
                         end_datetime    TIMESTAMPTZ,
                         cancelled_at    TIMESTAMPTZ,
                         cancel_reason   VARCHAR(255),
                         info_sent_at    TIMESTAMPTZ,
                         score           DOUBLE PRECISION,
                         summary         TEXT
);

CREATE TABLE advisors_in_session (
                                     session_id  INT NOT NULL,
                                     advisor_id  INT NOT NULL,
                                     PRIMARY KEY (session_id, advisor_id),
                                     CONSTRAINT fk_advisors_in_session_session FOREIGN KEY (session_id) REFERENCES session(id),
                                     CONSTRAINT fk_advisors_in_session_advisor FOREIGN KEY (advisor_id) REFERENCES advisor(id)
);

CREATE TABLE collaborators_in_session (
                                          session_id      INT NOT NULL,
                                          collaborator_id INT NOT NULL,
                                          PRIMARY KEY (session_id, collaborator_id),
                                          CONSTRAINT fk_collaborators_in_session_session      FOREIGN KEY (session_id)      REFERENCES session(id),
                                          CONSTRAINT fk_collaborators_in_session_collaborator FOREIGN KEY (collaborator_id) REFERENCES collaborator(id)
);

CREATE TABLE assistants (
                            session_id  INT     NOT NULL,
                            person_id   INT     NOT NULL,
                            registered  BOOLEAN,
                            attended    BOOLEAN,
                            PRIMARY KEY (session_id, person_id),
                            CONSTRAINT fk_assistants_session FOREIGN KEY (session_id) REFERENCES session(id),
                            CONSTRAINT fk_assistants_person  FOREIGN KEY (person_id)  REFERENCES person(id)
);