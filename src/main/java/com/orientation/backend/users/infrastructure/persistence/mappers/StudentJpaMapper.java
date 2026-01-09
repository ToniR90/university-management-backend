package com.orientation.backend.users.infrastructure.persistence.mappers;

import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.*;
import com.orientation.backend.users.domain.model.valueobjects.*;
import com.orientation.backend.users.infrastructure.persistence.entities.StudentJpaEntity;

import java.util.Optional;

public class StudentJpaMapper {

    // ========== Constructor ==========
    private StudentJpaMapper() {
    }

    // ========== JPA -> Domain ==========
    public static Student toDomain(StudentJpaEntity jpaEntity) {
        // TODO: Implementar conversión JPA → Domain
        // Necesitas convertir 20 campos

        // 1. Value Objects simples
        Dni dni = Dni.of(jpaEntity.getDni());

        // 2. Optional Value Objects
        Optional<Email> email = Optional.ofNullable(jpaEntity.getEmail()).map(Email::of);
        Optional<Phone> phone = Optional.ofNullable(jpaEntity.getPhone()).map(Phone::of);

        // 3. Composite Value Objects
        FullName fullName = FullName.of(jpaEntity.getName(), jpaEntity.getFirstSurname(), jpaEntity.getSecondSurname());

        // 4. AlumniInfo (factory methods)
        AlumniInfo alumniInfo;
        if (jpaEntity.getIsAlumni()) {
            AlumniType type = AlumniType.valueOf(jpaEntity.getAlumniType());
            Integer year = jpaEntity.getGraduationYear();
            alumniInfo = AlumniInfo.createAlumni(type, year);
        } else {
            alumniInfo = AlumniInfo.notAlumni();
        }

        // 5. RgpdConsent (factory methods)
        RgpdConsent rgpdConsent;
        RgpdConsentStatus status = RgpdConsentStatus.valueOf(jpaEntity.getRgpdConsentStatus());

        switch (status) {
            case PENDING -> rgpdConsent = RgpdConsent.pending();
            case SIGNED_IN_PERSON -> rgpdConsent = RgpdConsent.signedInPerson();
            case SIGNED_ONLINE -> rgpdConsent = RgpdConsent.signedOnline();
            case ALREADY_SIGNED -> rgpdConsent = RgpdConsent.alreadySigned(jpaEntity.getRgpdSignedYear());
            default -> throw new IllegalStateException("Unknown RGPD status: " + status);
        }

        // 6. Enums simples
        CurrentYear currentYear = CurrentYear.valueOf(jpaEntity.getCurrentYear());

        // 7. Optional Enums
        Optional<DiscoveryChannel> howDidYouKnowUs = Optional.ofNullable(jpaEntity.getHowDidYouKnowUs()).map(DiscoveryChannel::valueOf);
        Optional<ContactMethod> howDidYouContactUs = Optional.ofNullable(jpaEntity.getHowDidYouContactUs()).map(ContactMethod::valueOf);

        // 8. Optional String
        Optional<String> counselorNotes = Optional.ofNullable(jpaEntity.getCounselorNotes());

        // 9. Student Builder
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
}