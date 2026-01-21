package com.orientation.backend.users.infrastructure.persistence.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class StudentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    // ========== Constructor ==========
    public StudentJpaEntity () {}

    public StudentJpaEntity(Long id, String dni, String name, String firstSurname, String secondSurname,
                            String email, String phone, String degree, String currentYear, Boolean isAlumni,
                            String alumniType, Integer graduationYear, String rgpdConsentStatus,
                            LocalDateTime rgpdSignedDate, Integer rgpdSignedYear, String howDidYouKnowUs,
                            String howDidYouContactUs, String counselorNotes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.dni = dni;
        this.name = name;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.email = email;
        this.phone = phone;
        this.degree = degree;
        this.currentYear = currentYear;
        this.isAlumni = isAlumni;
        this.alumniType = alumniType;
        this.graduationYear = graduationYear;
        this.rgpdConsentStatus = rgpdConsentStatus;
        this.rgpdSignedDate = rgpdSignedDate;
        this.rgpdSignedYear = rgpdSignedYear;
        this.howDidYouKnowUs = howDidYouKnowUs;
        this.howDidYouContactUs = howDidYouContactUs;
        this.counselorNotes = counselorNotes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ========== Getters - Setters ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public Boolean getIsAlumni() {
        return isAlumni;
    }

    public void setAlumni(Boolean alumni) {
        isAlumni = alumni;
    }

    public String getAlumniType() {
        return alumniType;
    }

    public void setAlumniType(String alumniType) {
        this.alumniType = alumniType;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getRgpdConsentStatus() {
        return rgpdConsentStatus;
    }

    public void setRgpdConsentStatus(String rgpdConsentStatus) {
        this.rgpdConsentStatus = rgpdConsentStatus;
    }

    public LocalDateTime getRgpdSignedDate() {
        return rgpdSignedDate;
    }

    public void setRgpdSignedDate(LocalDateTime rgpdSignedDate) {
        this.rgpdSignedDate = rgpdSignedDate;
    }

    public Integer getRgpdSignedYear() {
        return rgpdSignedYear;
    }

    public void setRgpdSignedYear(Integer rgpdSignedYear) {
        this.rgpdSignedYear = rgpdSignedYear;
    }

    public String getHowDidYouKnowUs() {
        return howDidYouKnowUs;
    }

    public void setHowDidYouKnowUs(String howDidYouKnowUs) {
        this.howDidYouKnowUs = howDidYouKnowUs;
    }

    public String getHowDidYouContactUs() {
        return howDidYouContactUs;
    }

    public void setHowDidYouContactUs(String howDidYouContactUs) {
        this.howDidYouContactUs = howDidYouContactUs;
    }

    public String getCounselorNotes() {
        return counselorNotes;
    }

    public void setCounselorNotes(String counselorNotes) {
        this.counselorNotes = counselorNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

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
