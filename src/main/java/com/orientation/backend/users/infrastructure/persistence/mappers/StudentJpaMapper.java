package com.orientation.backend.users.infrastructure.persistence.mappers;

import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.*;
import com.orientation.backend.users.domain.model.valueobjects.*;
import com.orientation.backend.users.infrastructure.persistence.entities.StudentJpaEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public class StudentJpaMapper {

    // ========== Constructor ==========
    private StudentJpaMapper() {
    }

    // ========== JPA -> Domain ==========
    public static Student toDomain(StudentJpaEntity jpaEntity) {
        Dni dni = Dni.of(jpaEntity.getDni());

        Optional<Email> email = Optional.ofNullable(jpaEntity.getEmail()).map(Email::of);
        Optional<Phone> phone = Optional.ofNullable(jpaEntity.getPhone()).map(Phone::of);

        FullName fullName = FullName.of(jpaEntity.getName(), jpaEntity.getFirstSurname(), jpaEntity.getSecondSurname());

        AlumniInfo alumniInfo;
        if (jpaEntity.getIsAlumni()) {
            AlumniType type = AlumniType.valueOf(jpaEntity.getAlumniType());
            Integer year = jpaEntity.getGraduationYear();
            alumniInfo = AlumniInfo.createAlumni(type, year);
        } else {
            alumniInfo = AlumniInfo.notAlumni();
        }

        RgpdConsent rgpdConsent;
        RgpdConsentStatus status = RgpdConsentStatus.valueOf(jpaEntity.getRgpdConsentStatus());

        switch (status) {
            case PENDING -> rgpdConsent = RgpdConsent.pending();
            case SIGNED_IN_PERSON -> rgpdConsent = RgpdConsent.signedInPerson(jpaEntity.getRgpdSignedDate());
            case SIGNED_ONLINE -> rgpdConsent = RgpdConsent.signedOnline(jpaEntity.getRgpdSignedDate());
            case ALREADY_SIGNED -> rgpdConsent = RgpdConsent.alreadySigned(jpaEntity.getRgpdSignedYear());
            default -> throw new IllegalStateException("Unknown RGPD status: " + status);
        }

        CurrentYear currentYear = CurrentYear.valueOf(jpaEntity.getCurrentYear());

        Optional<DiscoveryChannel> howDidYouKnowUs = Optional.ofNullable(jpaEntity.getHowDidYouKnowUs()).map(DiscoveryChannel::valueOf);
        Optional<ContactMethod> howDidYouContactUs = Optional.ofNullable(jpaEntity.getHowDidYouContactUs()).map(ContactMethod::valueOf);

        Optional<String> counselorNotes = Optional.ofNullable(jpaEntity.getCounselorNotes());

        return Student.builder()
                .id(jpaEntity.getId())
                .dni(dni)
                .fullName(fullName)
                .email(email)
                .phone(phone)
                .degree(jpaEntity.getDegree())
                .currentYear(currentYear)
                .alumniInfo(alumniInfo)
                .rgpdConsent(rgpdConsent)
                .howDidYouKnowUs(howDidYouKnowUs)
                .howDidYouContactUs(howDidYouContactUs)
                .counselorNotes(counselorNotes)
                .createdAt(jpaEntity.getCreatedAt())
                .updatedAt(jpaEntity.getUpdatedAt())
                .build();
    }

    // ========== Domain -> Jpa ==========
    public static StudentJpaEntity toJpaEntity(Student student) {

        String dni = student.getDni().getValue();

        String name = student.getFullName().getName();
        String firstSurname = student.getFullName().getFirstSurname();
        String secondSurname = student.getFullName().getSecondSurname().orElse(null);

        String email = student.getEmail().map(Email::getValue).orElse(null);
        String phone = student.getPhone().map(Phone::getValue).orElse(null);

        String degree = student.getDegree();

        String currentYear = student.getCurrentYear().name();

        Boolean isAlumni = student.getAlumniInfo().isAlumni();
        String alumniType = student.getAlumniInfo().getType().map(AlumniType::name).orElse(null);
        Integer graduationYear = student.getAlumniInfo().getGraduationYear().orElse(null);

        String rgpdConsentStatus = student.getRgpdConsent().getStatus().name();
        LocalDateTime rgpdSignedDate = student.getRgpdConsent().getSignedDate().orElse(null);
        Integer rgpdSignedYear = student.getRgpdConsent().getSignedYear().orElse(null);

        String howDidYouKnowUs = student.getHowDidYouKnowUs().map(DiscoveryChannel::name).orElse(null);
        String howDidYouContactUs = student.getHowDidYouContactUs().map(ContactMethod::name).orElse(null);

        String counselorNotes = student.getCounselorNotes().orElse(null);

        LocalDateTime createdAt = student.getCreatedAt();
        LocalDateTime updatedAt = student.getUpdatedAt();

        StudentJpaEntity jpaEntity = new StudentJpaEntity();

        jpaEntity.setId(student.getId());
        jpaEntity.setDni(dni);
        jpaEntity.setName(name);
        jpaEntity.setFirstSurname(firstSurname);
        jpaEntity.setSecondSurname(secondSurname);
        jpaEntity.setEmail(email);
        jpaEntity.setPhone(phone);
        jpaEntity.setDegree(degree);
        jpaEntity.setCurrentYear(currentYear);
        jpaEntity.setAlumni(isAlumni);
        jpaEntity.setAlumniType(alumniType);
        jpaEntity.setGraduationYear(graduationYear);
        jpaEntity.setRgpdConsentStatus(rgpdConsentStatus);
        jpaEntity.setRgpdSignedDate(rgpdSignedDate);
        jpaEntity.setRgpdSignedYear(rgpdSignedYear);
        jpaEntity.setHowDidYouKnowUs(howDidYouKnowUs);
        jpaEntity.setHowDidYouContactUs(howDidYouContactUs);
        jpaEntity.setCounselorNotes(counselorNotes);
        jpaEntity.setCreatedAt(createdAt);
        jpaEntity.setUpdatedAt(updatedAt);

        return jpaEntity;
    }
}