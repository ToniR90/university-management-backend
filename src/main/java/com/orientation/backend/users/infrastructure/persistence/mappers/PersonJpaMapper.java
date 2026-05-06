package com.orientation.backend.users.infrastructure.persistence.mappers;

import com.orientation.backend.users.domain.model.entities.Person;
import com.orientation.backend.users.domain.model.enums.ContactOption;
import com.orientation.backend.users.domain.model.enums.DiscoveryOption;
import com.orientation.backend.users.domain.model.enums.RgpdConsentStatus;
import com.orientation.backend.users.domain.model.valueobjects.*;
import com.orientation.backend.users.infrastructure.persistence.entities.PersonJpaEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Optional;

@UtilityClass
public class PersonJpaMapper {

    // ========== JPA -> Domain ==========
    public static void fillBuilder(PersonJpaEntity jpaEntity, Person.Builder<?> builder){

        RgpdConsent rgpdConsent;
        RgpdConsentStatus status = RgpdConsentStatus.valueOf(jpaEntity.getRgpdConsentStatus());

        switch (status) {
            case PENDING -> rgpdConsent = RgpdConsent.pending();
            case SIGNED_IN_PERSON -> rgpdConsent = RgpdConsent.signedInPerson(jpaEntity.getRgpdSignedDate());
            case SIGNED_ONLINE -> rgpdConsent = RgpdConsent.signedOnline(jpaEntity.getRgpdSignedDate());
            case ALREADY_SIGNED -> rgpdConsent = RgpdConsent.alreadySigned(jpaEntity.getRgpdSignedYear());
            default -> throw new IllegalStateException("Unknown RGPD status: " + status);
        }

        Optional<DiscoveryOption> howDidYouKnowUs = Optional.ofNullable(jpaEntity.getHowDidYouKnowUs()).map(DiscoveryOption::valueOf);
        Optional<ContactOption> howDidYouContactUs = Optional.ofNullable(jpaEntity.getHowDidYouContactUs()).map(ContactOption::valueOf);

        builder.id(jpaEntity.getId())
                .active(jpaEntity.isActive())
                .dni(Dni.of(jpaEntity.getDni()))
                .fullName(FullName.of(jpaEntity.getName(), jpaEntity.getSurname()))
                .email(Email.ofNullable(jpaEntity.getEmail()))
                .phone(Phone.ofNullable(jpaEntity.getPhone()))
                .rgpdConsent(rgpdConsent)
                .howDidYouKnowUs(howDidYouKnowUs)
                .howDidYouContactUs(howDidYouContactUs)
                .createdAt(jpaEntity.getCreatedAt())
                .updatedAt(jpaEntity.getUpdatedAt())
                .deletedAt(jpaEntity.getDeletedAt());

    }

    // ========== Domain -> Jpa ==========
    public static PersonJpaEntity fillJpaEntity(Person person){

        boolean active = person.isActive();
        String dni = person.getDni().getValue();

        String name = person.getFullName().getName();
        String surname = person.getFullName().getSurname();

        String email = person.getEmail().map(Email::getValue).orElse(null);
        String phone = person.getPhone().map(Phone::getValue).orElse(null);

        String rgpdConsentStatus = person.getRgpdConsent().getStatus().name();
        LocalDateTime rgpdSignedDate = person.getRgpdConsent().getSignedDate().orElse(null);
        Integer rgpdSignedYear = person.getRgpdConsent().getSignedYear().orElse(null);

        String howDidYouKnowUs = person.getHowDidYouKnowUs().map(DiscoveryOption::name).orElse(null);
        String howDidYouContactUs = person.getHowDidYouContactUs().map(ContactOption::name).orElse(null);

        LocalDateTime createdAt = person.getCreatedAt();
        LocalDateTime updateAt = person.getUpdatedAt();
        LocalDateTime deletedAt = person.getDeletedAt().orElse(null);


        PersonJpaEntity jpaEntity = new PersonJpaEntity();

        jpaEntity.setId(person.getId());
        jpaEntity.setActive(active);
        jpaEntity.setDni(dni);
        jpaEntity.setName(name);
        jpaEntity.setSurname(surname);
        jpaEntity.setEmail(email);
        jpaEntity.setPhone(phone);
        jpaEntity.setRgpdConsentStatus(rgpdConsentStatus);
        jpaEntity.setRgpdSignedDate(rgpdSignedDate);
        jpaEntity.setRgpdSignedYear(rgpdSignedYear);
        jpaEntity.setHowDidYouKnowUs(howDidYouKnowUs);
        jpaEntity.setHowDidYouContactUs(howDidYouContactUs);
        jpaEntity.setCreatedAt(createdAt);
        jpaEntity.setUpdatedAt(updateAt);
        jpaEntity.setDeletedAt(deletedAt);

        return jpaEntity;
    }
}
