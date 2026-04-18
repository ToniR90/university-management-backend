-- V7__uuid_ids_and_on_delete_constraints.sql
-- Recreates all tables with UUID primary keys and explicit ON DELETE behaviour

-- Drop existing tables in reverse dependency order
DROP TABLE IF EXISTS assistants;
DROP TABLE IF EXISTS collaborators_in_session;
DROP TABLE IF EXISTS advisors_in_session;
DROP TABLE IF EXISTS session;
DROP TABLE IF EXISTS note;
DROP TABLE IF EXISTS collaborator;
DROP TABLE IF EXISTS advisor;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS organization;
DROP TABLE IF EXISTS person;

-- ============================================================
-- PERSON
-- ============================================================
CREATE TABLE person (
                        id                  UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
                        active              BOOLEAN      NOT NULL DEFAULT TRUE,
                        dni                 VARCHAR(20)  NOT NULL UNIQUE,
                        name                VARCHAR(100) NOT NULL,
                        surname             VARCHAR(100) NOT NULL,
                        email               VARCHAR(255) NOT NULL UNIQUE,
                        phone               VARCHAR(20),
                        rgpd_consent_status VARCHAR(50)  NOT NULL DEFAULT 'PENDING'
                            CHECK (rgpd_consent_status IN ('SIGNED_IN_PERSON', 'SIGNED_ONLINE', 'PENDING', 'ALREADY_SIGNED')),
                        rgpd_signed_year    INTEGER,
                        rgpd_signed_date    TIMESTAMPTZ,
                        discovery_option    VARCHAR(100),
                        contact_option      VARCHAR(100),
                        created_at          TIMESTAMPTZ,
                        updated_at          TIMESTAMPTZ,
                        deleted_at          TIMESTAMPTZ,

                        CONSTRAINT chk_rgpd_already_signed CHECK (
                            (rgpd_consent_status != 'ALREADY_SIGNED')
                                OR (rgpd_consent_status = 'ALREADY_SIGNED' AND rgpd_signed_year IS NOT NULL)
                            ),
                        CONSTRAINT chk_rgpd_signed_date CHECK (
                            (rgpd_consent_status NOT IN ('SIGNED_IN_PERSON', 'SIGNED_ONLINE'))
                                OR (rgpd_consent_status IN ('SIGNED_IN_PERSON', 'SIGNED_ONLINE') AND rgpd_signed_date IS NOT NULL)
                            )
);

-- ============================================================
-- ORGANIZATION
-- ============================================================
CREATE TABLE organization (
                              id   UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
                              name VARCHAR(255) NOT NULL
);

-- ============================================================
-- STUDENT
-- ============================================================
CREATE TABLE student (
                         id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         person_id       UUID NOT NULL UNIQUE,
                         current_year    VARCHAR(50),
                         degree          VARCHAR(100),
                         is_alumni       BOOLEAN NOT NULL DEFAULT FALSE,
                         graduation_year INTEGER,
                         alumni_type     VARCHAR(50),
                         counselor_notes TEXT,

                         CONSTRAINT fk_student_person FOREIGN KEY (person_id)
                             REFERENCES person(id) ON DELETE CASCADE,

                         CONSTRAINT chk_alumni_info CHECK (
                             (is_alumni = FALSE AND alumni_type IS NULL AND graduation_year IS NULL)
                                 OR (is_alumni = TRUE AND alumni_type IS NOT NULL AND graduation_year IS NOT NULL)
                             )
);

-- ============================================================
-- ADVISOR
-- ============================================================
CREATE TABLE advisor (
                         id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         person_id UUID NOT NULL UNIQUE,

                         CONSTRAINT fk_advisor_person FOREIGN KEY (person_id)
                             REFERENCES person(id) ON DELETE CASCADE
);

-- ============================================================
-- COLLABORATOR
-- ============================================================
CREATE TABLE collaborator (
                              id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              person_id       UUID NOT NULL UNIQUE,
                              external        BOOLEAN,
                              organization_id UUID,

                              CONSTRAINT fk_collaborator_person       FOREIGN KEY (person_id)
                                  REFERENCES person(id) ON DELETE CASCADE,
                              CONSTRAINT fk_collaborator_organization FOREIGN KEY (organization_id)
                                  REFERENCES organization(id) ON DELETE SET NULL
);

-- ============================================================
-- NOTE
-- ============================================================
CREATE TABLE note (
                      id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                      person_id  UUID,
                      created_by UUID,
                      content    TEXT        NOT NULL,
                      created_at TIMESTAMPTZ,

                      CONSTRAINT fk_note_person  FOREIGN KEY (person_id)
                          REFERENCES person(id) ON DELETE SET NULL,
                      CONSTRAINT fk_note_advisor FOREIGN KEY (created_by)
                          REFERENCES advisor(id) ON DELETE SET NULL
);

-- ============================================================
-- SESSION
-- ============================================================
CREATE TABLE session (
                         id             UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
                         title          VARCHAR(255) NOT NULL,
                         description    TEXT,
                         motivation     TEXT,
                         session_type   VARCHAR(50)  NOT NULL,
                         session_origin VARCHAR(50)  NOT NULL,
                         all_welcome    BOOLEAN,
                         start_datetime TIMESTAMPTZ,
                         end_datetime   TIMESTAMPTZ,
                         cancelled_at   TIMESTAMPTZ,
                         cancel_reason  VARCHAR(255),
                         info_sent_at   TIMESTAMPTZ,
                         score          DOUBLE PRECISION,
                         summary        TEXT
);

-- ============================================================
-- ADVISORS IN SESSION
-- ============================================================
CREATE TABLE advisors_in_session (
                                     session_id UUID NOT NULL,
                                     advisor_id UUID NOT NULL,
                                     PRIMARY KEY (session_id, advisor_id),

                                     CONSTRAINT fk_advisors_in_session_session FOREIGN KEY (session_id)
                                         REFERENCES session(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_advisors_in_session_advisor FOREIGN KEY (advisor_id)
                                         REFERENCES advisor(id) ON DELETE CASCADE
);

-- ============================================================
-- COLLABORATORS IN SESSION
-- ============================================================
CREATE TABLE collaborators_in_session (
                                          session_id      UUID NOT NULL,
                                          collaborator_id UUID NOT NULL,
                                          PRIMARY KEY (session_id, collaborator_id),

                                          CONSTRAINT fk_collaborators_in_session_session      FOREIGN KEY (session_id)
                                              REFERENCES session(id) ON DELETE CASCADE,
                                          CONSTRAINT fk_collaborators_in_session_collaborator FOREIGN KEY (collaborator_id)
                                              REFERENCES collaborator(id) ON DELETE CASCADE
);

-- ============================================================
-- ASSISTANTS
-- ============================================================
CREATE TABLE assistants (
                            session_id UUID    NOT NULL,
                            person_id  UUID    NOT NULL,
                            registered BOOLEAN,
                            attended   BOOLEAN,
                            PRIMARY KEY (session_id, person_id),

                            CONSTRAINT fk_assistants_session FOREIGN KEY (session_id)
                                REFERENCES session(id) ON DELETE CASCADE,
                            CONSTRAINT fk_assistants_person  FOREIGN KEY (person_id)
                                REFERENCES person(id) ON DELETE CASCADE
);