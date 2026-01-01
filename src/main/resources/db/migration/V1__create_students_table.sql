-- Migration: Create students table
-- Date: 2025-12-02
-- Author: Toni Romero
-- Description: Initial schema for student management system - orientation department

-- =======================
-- TABLE: students
-- =======================

CREATE TABLE students (
    -- ============================================
    -- PRIMARY KEY
    -- ============================================
    id BIGSERIAL PRIMARY KEY,

    -- ============================================
    -- IDENTIFICATION AND CONTACT
    -- ============================================
    name VARCHAR(100) NOT NULL,
    first_surname VARCHAR(100) NOT NULL,
    second_surname VARCHAR(100),
    email VARCHAR(255),
    dni VARCHAR(12) UNIQUE NOT NULL,
    phone VARCHAR(20),

    -- ============================================
    -- ACADEMIC INFORMATION
    -- ============================================
    degree VARCHAR(100) NOT NULL,  -- TODO: No check, catalog will be implemented on Sprint 2
    current_year VARCHAR(20) NOT NULL CHECK (current_year IN ('FIRST', 'SECOND', 'THIRD', 'FOURTH', 'FIFTH', 'SIXTH', 'MASTER', 'DOCTORATE')),

    -- ============================================
    -- ALUMNI INFORMATION
    -- ============================================
    is_alumni BOOLEAN NOT NULL DEFAULT FALSE,
    alumni_type VARCHAR(30),  -- TODO: No check, catalog will be implemented on Sprint 2
    alumni_graduation_year SMALLINT,

    -- ============================================
    -- ORIENTATION DEPARTMENT MANAGEMENT
    -- ============================================
    how_did_you_know_us VARCHAR(100),  -- TODO: No check, catalog will be implemented on Sprint 2
    how_did_you_contact_us VARCHAR(100),  -- TODO: No check, catalog will be implemented on Sprint 2
    counselor_notes TEXT,

    -- ============================================
    -- RGPD COMPLIANCE
    -- ============================================
    rgpd_consent_status VARCHAR(30) NOT NULL DEFAULT 'PENDING' CHECK (rgpd_consent_status IN ('SIGNED_IN_PERSON', 'SIGNED_ONLINE', 'PENDING', 'ALREADY_SIGNED')),
    rgpd_signed_year SMALLINT,
    rgpd_signed_date TIMESTAMPTZ,

    -- ============================================
    -- METADATA (AUDIT FIELDS)
    -- ============================================
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    -- ============================================
    -- CONSTRAINTS
    -- ============================================
    CONSTRAINT uq_student_email UNIQUE (email),
    CONSTRAINT chk_alumni_info CHECK (
        (is_alumni = FALSE AND alumni_type IS NULL AND alumni_graduation_year IS NULL)
        OR
        (is_alumni = TRUE AND alumni_type IS NOT NULL AND alumni_graduation_year IS NOT NULL)
    ),
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


-- =======================
-- INDEXES
-- =======================

-- Index on email (only when email is not null)
CREATE INDEX idx_students_email ON students(email) WHERE email IS NOT NULL;

-- Index on dni: NOT NEEDED - automatically created by UNIQUE constraint
-- dni VARCHAR(12) UNIQUE NOT NULL already has an index

-- Index on degree (filtering and statistics)
CREATE INDEX idx_students_degree ON students(degree);

-- =======================
-- OPTIONAL INDEXES (Sprint 2 - Add if needed)
-- =======================
-- Uncomment these if filtering becomes frequent:
-- CREATE INDEX idx_students_is_alumni ON students(is_alumni);
-- CREATE INDEX idx_students_current_year ON students(current_year);

-- =======================
-- COMMENTS (Documentation)
-- =======================

-- Table comment
COMMENT ON TABLE students IS 'Students managed by the orientation department';

-- Column comments (ejemplos - completa el resto)
COMMENT ON COLUMN students.id IS 'Unique identifier (auto-generated)';
COMMENT ON COLUMN students.dni IS 'Spanish DNI or NIE (unique identifier, primary search field)';
COMMENT ON COLUMN students.email IS 'Email address (optional, can be added later)';
COMMENT ON COLUMN students.second_surname IS 'Second surname (optional, not all students have it)';
COMMENT ON COLUMN students.current_year IS 'Current academic year: FIRST, SECOND, ..., SIXTH, MASTER, DOCTORATE';
COMMENT ON COLUMN students.is_alumni IS 'Whether the student is an alumnus';
COMMENT ON COLUMN students.alumni_type IS 'Type of alumni (only if is_alumni = true): BACHELOR, MASTER, DOCTORATE, DOUBLE_DEGREE, ERASMUS, EXCHANGE, OTHER';
COMMENT ON COLUMN students.alumni_graduation_year IS 'Year when the student graduated (only if is_alumni = true)';
COMMENT ON COLUMN students.name IS 'Student first name';
COMMENT ON COLUMN students.first_surname IS 'Student first surname';
COMMENT ON COLUMN students.phone IS 'Contact phone number (optional)';
COMMENT ON COLUMN students.degree IS 'Academic degree or program (e.g., "Ingeniería Informática") - Catalog in Sprint 2';
COMMENT ON COLUMN students.how_did_you_know_us IS 'How the student learned about the orientation department';
COMMENT ON COLUMN students.how_did_you_contact_us IS 'How the student contacted the department';
COMMENT ON COLUMN students.counselor_notes IS 'Internal notes from the counselor about the student';
COMMENT ON COLUMN students.rgpd_consent_status IS 'RGPD consent status: SIGNED_IN_PERSON, SIGNED_ONLINE, PENDING, ALREADY_SIGNED';
COMMENT ON COLUMN students.rgpd_signed_year IS 'Year when RGPD was signed (for ALREADY_SIGNED status, must be 2018 or later)';
COMMENT ON COLUMN students.rgpd_signed_date IS 'Exact date when RGPD was signed (for SIGNED_IN_PERSON or SIGNED_ONLINE)';
COMMENT ON COLUMN students.created_at IS 'Timestamp when the record was created';
COMMENT ON COLUMN students.updated_at IS 'Timestamp when the record was last updated (managed by application)';