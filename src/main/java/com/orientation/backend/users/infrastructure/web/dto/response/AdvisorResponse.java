package com.orientation.backend.users.infrastructure.web.dto.response;

import com.orientation.backend.users.domain.model.entities.Advisor;

import java.time.LocalDateTime;

/**
 * DTO for API responses containing advisor data.
 * Flat structure matching the API contract.
 */
public record AdvisorResponse (
        String dni,
        String name,
        String surname,
        String email,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){

    /**
     * Factory method to create an AdvisorResponse from a domain Student entity.
     */
    public static AdvisorResponse fromDomain(Advisor advisor){
        return new AdvisorResponse(
                advisor.getDni().getValue(),
                advisor.getFullName().getName(),
                advisor.getFullName().getSurname(),
                advisor.getEmail().map(e -> e.getValue()).orElse(null),
                advisor.getPhone().map(p -> p.getValue()).orElse(null),
                advisor.getCreatedAt(),
                advisor.getUpdatedAt()
        );
    }
}
