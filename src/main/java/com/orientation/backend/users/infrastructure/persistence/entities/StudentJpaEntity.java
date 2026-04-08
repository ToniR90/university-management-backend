package com.orientation.backend.users.infrastructure.persistence.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "active")
    private boolean active;

    @Column(name = "dni", nullable = false, unique = true)
    private String dni;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "first_surname", nullable = false)
    private String firstSurname;

    @Column(name = "second_surname")
    private String secondSurname;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "degree", nullable = false)
    private String degree;

    @Column(name = "current_year", nullable = false)
    private String currentYear;

    @Column(name = "is_alumni", nullable = false)
    private Boolean isAlumni;

    @Column(name = "alumni_type")
    private String alumniType;

    @Column(name = "alumni_graduation_year")
    private Integer graduationYear;

    @Column(name = "rgpd_consent_status", nullable = false)
    private String rgpdConsentStatus;

    @Column(name = "rgpd_signed_date")
    private LocalDateTime rgpdSignedDate;

    @Column(name = "rgpd_signed_year")
    private Integer rgpdSignedYear;

    @Column(name = "how_did_you_know_us")
    private String howDidYouKnowUs;

    @Column(name = "how_did_you_contact_us")
    private String howDidYouContactUs;

    @Column(name = "counselor_notes", columnDefinition = "TEXT")
    private String counselorNotes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // ========== Callbacks ==========

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
