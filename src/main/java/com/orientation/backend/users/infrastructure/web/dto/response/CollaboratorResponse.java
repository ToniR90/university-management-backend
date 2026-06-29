package com.orientation.backend.users.infrastructure.web.dto.response;

import com.orientation.backend.users.domain.model.entities.Collaborator;

import java.time.LocalDateTime;

public record CollaboratorResponse (
        String dni,
        String name,
        String surname,
        String email,
        String phone,
        boolean external,
        String organizationName,
        LocalDateTime createdAt
){

    public static CollaboratorResponse fromDomain(Collaborator collaborator) {
        return new CollaboratorResponse(
                collaborator.getDni().getValue(),
                collaborator.getFullName().getName(),
                collaborator.getFullName().getSurname(),
                collaborator.getEmail().map(e -> e.getValue()).orElse(null),
                collaborator.getPhone().map(p -> p.getValue()).orElse(null),
                collaborator.isExternal(),
                collaborator.getOrganization() != null ? collaborator.getOrganization().getName() : null,
                collaborator.getCreatedAt()
        );
    }
}
