package com.orientation.backend.users.domain.model.entities;

import com.orientation.backend.users.domain.model.enums.ContactOption;
import com.orientation.backend.users.domain.model.enums.DiscoveryOption;
import com.orientation.backend.users.domain.model.exceptions.PersonAlreadyInactiveException;
import com.orientation.backend.users.domain.model.valueobjects.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public abstract class Person {

    protected UUID id;
    protected boolean active;
    protected final Dni dni;
    protected final FullName fullName;
    protected Email email;
    protected Phone phone;
    protected RgpdConsent rgpdConsent;
    protected DiscoveryOption howDidYouKnowUs;
    protected ContactOption howDidYouContactUs;
    protected final LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected LocalDateTime deletedAt;


    // CONSTRUCTOR (Private - Use Builder)
    protected Person(Builder builder) {
        this.id = builder.id;
        this.active = builder.active;
        this.dni = Objects.requireNonNull(builder.dni, "DNI cannot be null");
        this.fullName = Objects.requireNonNull(builder.fullName, "FullName cannot be null");
        this.email = builder.email;
        this.phone = builder.phone;
        this.rgpdConsent = Objects.requireNonNull(builder.rgpdConsent, "RGPD consent cannot be null");
        this.howDidYouKnowUs = builder.howDidYouKnowUs;
        this.howDidYouContactUs = builder.howDidYouContactUs;
        this.createdAt = (builder.createdAt != null) ? builder.createdAt : LocalDateTime.now();
        this.updatedAt = (builder.updatedAt != null) ? builder.updatedAt : LocalDateTime.now();
        this.deletedAt = builder.deletedAt;
    }


    // ============================================
    // BUSINESS METHODS - Email Management
    // ============================================
    /**
     * Adds email to person (if not present).
     *
     * @param email the email to add
     * @throws IllegalStateException if email already exists
     */
    public void addEmail(Email email) {
        Objects.requireNonNull(email, "Email cannot be null");

        if (this.email != null) {
            throw new IllegalStateException("There's already an email declared, use updateEmail for update");
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
        Objects.requireNonNull(newEmail, "New email cannot be null");

        if (this.email == null) {
            throw new IllegalStateException("There's no email declared, use addEmail");
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
            throw new IllegalStateException("There's no email to delete");
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
        Objects.requireNonNull(phone, "Phone cannot be null");

        if (this.phone != null) {
            throw new IllegalStateException("There's already a phone declared");
        }

        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Updates existing phone.
     */
    public void updatePhone(Phone newPhone) {
        Objects.requireNonNull(newPhone, "New phone cannot be null");

        if (this.phone == null) {
            throw new IllegalStateException("There's no phone declared");
        }

        this.phone = newPhone;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Removes phone from student.
     */
    public void removePhone() {
        if (this.phone == null) {
            throw new IllegalStateException("There's no phone declared");
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

    /**
     * Updates RGPD consent with a new consent object.
     */
    public void updateRgpdConsent(RgpdConsent newConsent) {
        this.rgpdConsent = Objects.requireNonNull(newConsent, "El consentiment RGPD no pot ser nul");
        this.updatedAt = LocalDateTime.now();
    }


    // ============================================
    // BUSINESS METHODS - Discovery & Contact Tracking
    // ============================================
    /**
     * Registers how the student discovered the department.
     */
    public void registerDiscoveryChannel(DiscoveryOption channel) {
        this.howDidYouKnowUs = channel;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Registers how the student contacted the department.
     */
    public void registerContactMethod(ContactOption method) {
        this.howDidYouContactUs = method;
        this.updatedAt = LocalDateTime.now();
    }


    // ============================================
    // BUSINESS METHODS - Soft Delete Method
    // ============================================
    public void deactivate() {
        if (!this.active) {
            throw new PersonAlreadyInactiveException();
        }
        this.active = false;
        this.deletedAt = LocalDateTime.now();
    }


    // ============================================
    // GETTERS (Read-Only Access)
    // ============================================
    public UUID getId() {
        return id;
    }

    public boolean isActive() {
        return active;
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

    public RgpdConsent getRgpdConsent() {
        return rgpdConsent;
    }

    public Optional<DiscoveryOption> getHowDidYouKnowUs() {
        return Optional.ofNullable(howDidYouKnowUs);
    }

    public Optional<ContactOption> getHowDidYouContactUs() {
        return Optional.ofNullable(howDidYouContactUs);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Optional<LocalDateTime> getDeletedAt() {
        return Optional.ofNullable(deletedAt);
    }

    // =======================================================================

    // ============================================
    // BUILDER (Inner Static Class)
    // ============================================

    public static class Builder<T extends Builder<T>> {
        private UUID id;
        private boolean active;
        private Dni dni;
        private FullName fullName;
        private Email email;
        private Phone phone;
        private RgpdConsent rgpdConsent;
        private DiscoveryOption howDidYouKnowUs;
        private ContactOption howDidYouContactUs;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime deletedAt;

        protected Builder() {
            this.active = true;
            this.rgpdConsent = RgpdConsent.pending();
        }

        public T id(UUID id) {
            this.id = id;
            return (T) this;
        }

        public T active(boolean active) {
            this.active = active;
            return (T) this;
        }

        public T dni(Dni dni) {
            this.dni = dni;
            return (T) this;
        }

        public T fullName(FullName fullName) {
            this.fullName = fullName;
            return (T) this;
        }

        public T email(Optional<Email> email) {
            this.email = email.orElse(null);
            return (T) this;
        }

        // OverCharge for direct Email
        public T email(Email email) {
            this.email = email;
            return (T) this;
        }

        public T phone(Optional<Phone> phone) {
            this.phone = phone.orElse(null);
            return (T) this;
        }

        // OverCharge for direct Phone
        public T phone(Phone phone) {
            this.phone = phone;
            return (T) this;
        }

        public T rgpdConsent(RgpdConsent rgpdConsent) {
            this.rgpdConsent = rgpdConsent;
            return (T) this;
        }

        public T howDidYouKnowUs(Optional<DiscoveryOption> howDidYouKnowUs) {
            this.howDidYouKnowUs = howDidYouKnowUs.orElse(null);
            return (T) this;
        }

        // OverCharge for direct input
        public T howDidYouKnowUs(DiscoveryOption howDidYouKnowUs) {
            this.howDidYouKnowUs = howDidYouKnowUs;
            return (T) this;
        }

        public T howDidYouContactUs(Optional<ContactOption> howDidYouContactUs) {
            this.howDidYouContactUs = howDidYouContactUs.orElse(null);
            return (T) this;
        }

        // OverCharge for direct input
        public T howDidYouContactUs(ContactOption howDidYouContactUs) {
            this.howDidYouContactUs = howDidYouContactUs;
            return (T) this;
        }

        public T createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return (T) this;
        }

        public T updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return (T) this;
        }

        public T deletedAt(LocalDateTime deletedAt) {
            this.deletedAt = deletedAt;
            return (T) this;
        }

        /**
         * Builds the Person instance.
         * Validates all required fields are present.
         *
         * @return new Person instance
         * @throws NullPointerException if required fields are null
         * @throws IllegalArgumentException if validation fails
         */

    }


    // ============================================
    // EQUALS & HASHCODE (By ID - Entity)
    // ============================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;

        if (id != null && person.id != null) {
            return Objects.equals(id, person.id);
        }

        return Objects.equals(dni, person.dni);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : Objects.hash(dni);
    }

    @Override
    public String toString() {
        return "Persona: " + fullName + "\n" +
                "Dni: " + dni + "\n" +
                "Email: " + email;
    }
}
