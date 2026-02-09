package com.orientation.backend.users.infrastructure.web.dto.request;

import com.orientation.backend.users.domain.model.entities.Student;

import java.time.LocalDateTime;

/**
 * DTO for API responses containing student data.
 * Flat structure matching the API contract.
 */
public record StudentResponse(
        Long id,
        String dni,
        String name,
        String firstSurname,
        String secondSurname,
        String email,
        String phone,
        String degree,
        String currentYear,
        boolean isAlumni,
        String alumniType,
        Integer alumniGraduationYear,
        String rgpdConsentStatus,
        Integer rgpdSignedYear,
        LocalDateTime rgpdSignedDate,
        String howDidYouKnowUs,
        String howDidYouContactUs,
        String counselorNotes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    /**
     * Factory method to create a StudentResponse from a domain Student entity.
     */
    public static StudentResponse fromDomain(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getDni().getValue(),
                student.getFullName().getName(),
                student.getFullName().getFirstSurname(),
                student.getFullName().getSecondSurname().orElse(null),
                student.getEmail().map(e -> e.getValue()).orElse(null),
                student.getPhone().map(p -> p.getValue()).orElse(null),
                student.getDegree(),
                student.getCurrentYear().name(),
                student.getAlumniInfo().isAlumni(),
                student.getAlumniInfo().getType().map(Enum::name).orElse(null),
                student.getAlumniInfo().getGraduationYear().orElse(null),
                student.getRgpdConsent().getStatus().name(),
                student.getRgpdConsent().getSignedYear().orElse(null),
                student.getRgpdConsent().getSignedDate().orElse(null),
                student.getHowDidYouKnowUs().map(Enum::name).orElse(null),
                student.getHowDidYouContactUs().map(Enum::name).orElse(null),
                student.getCounselorNotes().orElse(null),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}