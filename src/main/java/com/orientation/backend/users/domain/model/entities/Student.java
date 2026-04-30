package com.orientation.backend.users.domain.model.entities;

import com.orientation.backend.users.domain.model.enums.*;
import com.orientation.backend.users.domain.model.valueobjects.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Student Aggregate Root.
 * Represents a student managed by the orientation department.
 * Invariants:
 * - dni must be unique and valid
 * - fullName cannot be empty
 * - currentYear must be valid
 * - rgpdConsent is always present (at least PENDING)
 * - alumniInfo is always present (at least notAlumni())
 * - createdAt is immutable
 * - updatedAt is updated on every modification
 *
 * Immutable fields: id, dni, fullName, currentYear, createdAt
 * Mutable fields: email, phone, alumniInfo, rgpdConsent, discovery/contact, notes, updatedAt
 */
public class Student extends Person{

    // Academic Info
    private final CurrentYear currentYear;
    private Degree degree;

    // Composite Value Objects
    private AlumniInfo alumniInfo;

    // Notes
    private String counselorNotes;



    // CONSTRUCTOR (Private - use Builder)
    private Student(Builder builder) {
        super(builder);
        this.currentYear = Objects.requireNonNull(builder.currentYear, "El curs actual no pot ser nul");
        this.degree = Objects.requireNonNull(builder.degree);
        this.alumniInfo = Objects.requireNonNull(builder.alumniInfo, "La informació d'alumni no pot ser nul");
        this.counselorNotes = builder.counselorNotes;
    }

    // ============================================
    // FACTORY METHOD (Builder)
    // ============================================

    public static Builder builder() {
        return new Builder();
    }


    // ============================================
    // BUSINESS METHODS - Alumni Management
    // ============================================

    /**
     * Marks student as alumni.
     *
     * @param type alumni type
     * @param graduationYear year of graduation
     */
    public void markAsAlumni(AlumniType type, Integer graduationYear) {
        this.alumniInfo = AlumniInfo.createAlumni(type, graduationYear);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Marks student as non-alumni (reverts alumni status).
     */
    public void markAsNonAlumni() {
        this.alumniInfo = AlumniInfo.notAlumni();
        this.updatedAt = LocalDateTime.now();
    }


    // ============================================
    // BUSINESS METHODS - Notes Management
    // ============================================

    /**
     * Updates counselor notes about the student.
     *
     * @param notes the notes (can be null to clear)
     */
    public void updateCounselorNotes(String notes) {
        this.counselorNotes = (notes != null) ? notes.trim() : null;
        this.updatedAt = LocalDateTime.now();
    }


    // ============================================
    // GETTERS (Read-Only Access)
    // ============================================
    public Degree getDegree() {
        return degree;
    }

    public CurrentYear getCurrentYear() {
        return currentYear;
    }

    public AlumniInfo getAlumniInfo() {
        return alumniInfo;
    }

    public Optional<String> getCounselorNotes() {
        return Optional.ofNullable(counselorNotes);
    }


    // ============================================
    // BUILDER (Inner Static Class)
    // ============================================

    public static class Builder extends Person.Builder<Builder> {
        private Degree degree;
        private CurrentYear currentYear;
        private AlumniInfo alumniInfo;
        private String counselorNotes;

        private Builder() {
            this.alumniInfo = AlumniInfo.notAlumni();
        }

        public Builder degree(Degree degree) {
            this.degree = degree;
            return this;
        }

        public Builder currentYear(CurrentYear currentYear) {
            this.currentYear = currentYear;
            return this;
        }

        public Builder alumniInfo(AlumniInfo alumniInfo) {
            this.alumniInfo = alumniInfo;
            return this;
        }

        public Builder counselorNotes(Optional<String> counselorNotes) {
            this.counselorNotes = counselorNotes.orElse(null);
            return this;
        }

        // Overload for direct input
        public Builder counselorNotes(String counselorNotes) {
            this.counselorNotes = counselorNotes;
            return this;
        }

        /**
         * Builds the Student instance.
         * Validates all required fields are present.
         *
         * @return new Student instance
         * @throws NullPointerException if required fields are null
         * @throws IllegalArgumentException if validation fails
         */
        public Student build() {
            return new Student(this);
        }
    }

    // ============================================
    // EQUALS & HASHCODE (By ID - Entity)
    // ============================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;

        if (id != null && student.id != null) {
            return Objects.equals(id, student.id);
        }

        return Objects.equals(dni, student.dni);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : Objects.hash(dni);
    }

    @Override
    public String toString() {
        return "Estudiant: " + fullName + "\n" +
                "Dni: " + dni + "\n" +
                "Email: " + email + "\n" +
                "Grau: " + degree + "\n" +
                "Curs actual: " + currentYear + "\n" +
                "Alumni: " + alumniInfo.isAlumni();
    }
}