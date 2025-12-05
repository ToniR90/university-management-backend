package com.orientation.backend.users.domain.model.entities;

import com.orientation.backend.users.domain.model.enums.AlumniType;
import com.orientation.backend.users.domain.model.enums.ContactMethod;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.DiscoveryChannel;
import com.orientation.backend.users.domain.model.valueobjects.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Student Aggregate Root.
 * Represents a student managed by the orientation department.
 *
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
public class Student {
    // Identity
    private Long id;

    // Value Objects - Immutable identity
    private final Dni dni;
    private final FullName fullName;
    private final CurrentYear currentYear;

    // Value Objects - Mutable state
    private Email email;
    private Phone phone;

    // Academic Info
    private String degree;  // TODO: Catalog in Sprint 2

    // Composite Value Objects
    private AlumniInfo alumniInfo;
    private RgpdConsent rgpdConsent;

    // Enums - Discovery & Contact
    private DiscoveryChannel howDidYouKnowUs;
    private ContactMethod howDidYouContactUs;

    // Notes
    private String counselorNotes;

    // Metadata
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // CONSTRUCTOR (Private - use Builder)
    private Student(Builder builder) {
        this.id = builder.id;
        this.dni = Objects.requireNonNull(builder.dni, "El DNI no pot ser nul");
        this.fullName = Objects.requireNonNull(builder.fullName, "El nom complet no pot ser nul");
        this.currentYear = Objects.requireNonNull(builder.currentYear, "El curs actual no pot ser nul");

        this.email = builder.email;
        this.phone = builder.phone;

        this.degree = validateDegree(builder.degree);

        this.alumniInfo = Objects.requireNonNull(builder.alumniInfo, "La informació d'alumni no pot ser nul");
        this.rgpdConsent = Objects.requireNonNull(builder.rgpdConsent, "El consentiment RGPD no pot ser nul");

        this.howDidYouKnowUs = builder.howDidYouKnowUs;
        this.howDidYouContactUs = builder.howDidYouContactUs;
        this.counselorNotes = builder.counselorNotes;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ============================================
    // FACTORY METHOD (Builder)
    // ============================================

    public static Builder builder() {
        return new Builder();
    }

    // ============================================
    // BUSINESS METHODS - Email Management
    // ============================================

    /**
     * Adds email to student (if not present).
     *
     * @param email the email to add
     * @throws IllegalStateException if email already exists
     */
    public void addEmail(Email email) {
        Objects.requireNonNull(email, "El correu electrònic no pot ser nul");

        if (this.email != null) {
            throw new IllegalStateException("L'estudiant ja té un correu electrònic. Utilitza updateEmail() per canviar-lo");
        }

        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates existing email.
     *
     * @param newEmail the new email
     * @throws IllegalStateException if no email exists
     */
    public void updateEmail(Email newEmail) {
        Objects.requireNonNull(newEmail, "El nou correu electrònic no pot ser nul");

        if (this.email == null) {
            throw new IllegalStateException("L'estudiant no té correu electrònic. Utilitza addEmail() per afegir-ne un");
        }

        this.email = newEmail;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Removes email from student.
     *
     * @throws IllegalStateException if no email exists
     */
    public void removeEmail() {
        if (this.email == null) {
            throw new IllegalStateException("L'estudiant no té correu electrònic per eliminar");
        }

        this.email = null;
        this.updatedAt = LocalDateTime.now();
    }

    // ============================================
    // BUSINESS METHODS - Phone Management
    // ============================================

    /**
     * Adds phone to student (if not present).
     */
    public void addPhone(Phone phone) {
        Objects.requireNonNull(phone, "El telèfon no pot ser nul");

        if (this.phone != null) {
            throw new IllegalStateException("L'estudiant ja té un telèfon. Utilitza updatePhone() per canviar-lo");
        }

        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates existing phone.
     */
    public void updatePhone(Phone newPhone) {
        Objects.requireNonNull(newPhone, "El nou telèfon no pot ser nul");

        if (this.phone == null) {
            throw new IllegalStateException("L'estudiant no té telèfon. Utilitza addPhone() per afegir-ne un");
        }

        this.phone = newPhone;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Removes phone from student.
     */
    public void removePhone() {
        if (this.phone == null) {
            throw new IllegalStateException("L'estudiant no té telèfon per eliminar");
        }

        this.phone = null;
        this.updatedAt = LocalDateTime.now();
    }

    // ============================================
    // BUSINESS METHODS - Contact Info (Atomic)
    // ============================================

    /**
     * Updates contact information atomically.
     * Both email and phone can be null.
     */
    public void updateContactInfo(Email email, Phone phone) {
        this.email = email;
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }

    // ============================================
    // BUSINESS METHODS - RGPD Management
    // ============================================

    /**
     * Registers RGPD consent signed in person (now).
     */
    public void giveRgpdConsentInPerson() {
        this.rgpdConsent = RgpdConsent.signedInPerson();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Registers RGPD consent signed online (now).
     */
    public void giveRgpdConsentOnline() {
        this.rgpdConsent = RgpdConsent.signedOnline();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Registers previous RGPD consent (already signed in past year).
     *
     * @param year the year when consent was given (>= 2018)
     */
    public void registerPreviousRgpdConsent(Integer year) {
        this.rgpdConsent = RgpdConsent.alreadySigned(year);
        this.updatedAt = LocalDateTime.now();
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
    // BUSINESS METHODS - Discovery & Contact Tracking
    // ============================================

    /**
     * Registers how the student discovered the department.
     */
    public void registerDiscoveryChannel(DiscoveryChannel channel) {
        this.howDidYouKnowUs = channel;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Registers how the student contacted the department.
     */
    public void registerContactMethod(ContactMethod method) {
        this.howDidYouContactUs = method;
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

    public Long getId() {
        return id;
    }

    public Dni getDni() {
        return dni;
    }

    public FullName getFullName() {
        return fullName;
    }

    public Optional<Email> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<Phone> getPhone() {
        return Optional.ofNullable(phone);
    }

    public String getDegree() {
        return degree;
    }

    public CurrentYear getCurrentYear() {
        return currentYear;
    }

    public AlumniInfo getAlumniInfo() {
        return alumniInfo;
    }

    public RgpdConsent getRgpdConsent() {
        return rgpdConsent;
    }

    public Optional<DiscoveryChannel> getHowDidYouKnowUs() {
        return Optional.ofNullable(howDidYouKnowUs);
    }

    public Optional<ContactMethod> getHowDidYouContactUs() {
        return Optional.ofNullable(howDidYouContactUs);
    }

    public Optional<String> getCounselorNotes() {
        return Optional.ofNullable(counselorNotes);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ============================================
    // HELPER METHODS (Private)
    // ============================================

    private static String validateDegree(String degree) {
        Objects.requireNonNull(degree, "El grau no pot ser nul");
        String trimmed = degree.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("El grau no pot estar buit");
        }
        return trimmed;
    }

    // ============================================
    // BUILDER (Inner Static Class)
    // ============================================

    public static class Builder {
        private Long id;
        private Dni dni;
        private FullName fullName;
        private Email email;
        private Phone phone;
        private String degree;
        private CurrentYear currentYear;
        private AlumniInfo alumniInfo;
        private RgpdConsent rgpdConsent;
        private DiscoveryChannel howDidYouKnowUs;
        private ContactMethod howDidYouContactUs;
        private String counselorNotes;

        private Builder() {
            this.alumniInfo = AlumniInfo.notAlumni();
            this.rgpdConsent = RgpdConsent.pending();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder dni(Dni dni) {
            this.dni = dni;
            return this;
        }

        public Builder fullName(FullName fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder email(Email email) {
            this.email = email;
            return this;
        }

        public Builder phone(Phone phone) {
            this.phone = phone;
            return this;
        }

        public Builder degree(String degree) {
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

        public Builder rgpdConsent(RgpdConsent rgpdConsent) {
            this.rgpdConsent = rgpdConsent;
            return this;
        }

        public Builder howDidYouKnowUs(DiscoveryChannel howDidYouKnowUs) {
            this.howDidYouKnowUs = howDidYouKnowUs;
            return this;
        }

        public Builder howDidYouContactUs(ContactMethod howDidYouContactUs) {
            this.howDidYouContactUs = howDidYouContactUs;
            return this;
        }

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