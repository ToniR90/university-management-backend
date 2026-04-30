-- V4__extract_person_from_student.sql
-- Creates person, student (refactored), advisor, collaborator and organization tables

CREATE TABLE person (
                        id          SERIAL PRIMARY KEY,
                        active      BOOLEAN      NOT NULL DEFAULT TRUE,
                        dni         VARCHAR(20)  NOT NULL UNIQUE,
                        name        VARCHAR(100) NOT NULL,
                        surname     VARCHAR(100) NOT NULL,
                        email       VARCHAR(255) NOT NULL UNIQUE,
                        phone       VARCHAR(20),
                        rgpd_consent_status     VARCHAR(50)  NOT NULL DEFAULT 'PENDING' CHECK (rgpd_consent_status IN ('SIGNED_IN_PERSON', 'SIGNED_ONLINE', 'PENDING', 'ALREADY_SIGNED')),
                        rgpd_signed_year        INTEGER,
                        rgpd_signed_date        TIMESTAMPTZ,
                        discovery_option        VARCHAR(100),
                        contact_option          VARCHAR(100),
                        created_at  TIMESTAMPTZ,
                        updated_at  TIMESTAMPTZ,
                        deleted_at  TIMESTAMPTZ,

                        CONSTRAINT chk_rgpd_already_signed CHECK (
                            (rgpd_consent_status != 'ALREADY_SIGNED')
                                OR
                            (rgpd_consent_status = 'ALREADY_SIGNED' AND rgpd_signed_year IS NOT NULL)
                            ),
                        CONSTRAINT chk_rgpd_signed_date CHECK (
                            (rgpd_consent_status NOT IN ('SIGNED_IN_PERSON', 'SIGNED_ONLINE'))
                                OR
                            (rgpd_consent_status IN ('SIGNED_IN_PERSON', 'SIGNED_ONLINE') AND rgpd_signed_date IS NOT NULL)
                            )
);

CREATE TABLE organization (
                              id      SERIAL PRIMARY KEY,
                              name    VARCHAR(255) NOT NULL
);

CREATE TABLE student (
                         id              SERIAL PRIMARY KEY,
                         person_id       INT NOT NULL UNIQUE,
                         current_year    VARCHAR(50),
                         degree          VARCHAR(100),
                         is_alumni       BOOLEAN NOT NULL DEFAULT FALSE,
                         graduation_year INTEGER,
                         alumni_type     VARCHAR(50),
                         CONSTRAINT fk_student_person FOREIGN KEY (person_id) REFERENCES person(id),
                         CONSTRAINT chk_alumni_info CHECK (
                             (is_alumni = FALSE AND alumni_type IS NULL AND graduation_year IS NULL)
                                 OR
                             (is_alumni = TRUE AND alumni_type IS NOT NULL AND graduation_year IS NOT NULL)
                             )
);

CREATE TABLE advisor (
                         id        SERIAL PRIMARY KEY,
                         person_id INT NOT NULL UNIQUE,
                         CONSTRAINT fk_advisor_person FOREIGN KEY (person_id) REFERENCES person(id)
);

CREATE TABLE collaborator (
                              id              SERIAL PRIMARY KEY,
                              person_id       INT NOT NULL UNIQUE,
                              external        BOOLEAN,
                              organization_id INT,
                              CONSTRAINT fk_collaborator_person       FOREIGN KEY (person_id)       REFERENCES person(id),
                              CONSTRAINT fk_collaborator_organization FOREIGN KEY (organization_id) REFERENCES organization(id)
);