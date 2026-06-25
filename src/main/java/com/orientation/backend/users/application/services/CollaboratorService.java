package com.orientation.backend.users.application.services;

import com.orientation.backend.shared.application.exceptions.core.BusinessViolation;
import com.orientation.backend.shared.application.exceptions.core.ErrorCode;
import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.application.commands.collaborators.CreateCollaboratorCommand;
import com.orientation.backend.users.application.exceptions.collaborators.CreatedCollaboratorException;
import com.orientation.backend.users.domain.model.entities.Collaborator;
import com.orientation.backend.users.domain.model.entities.Organization;
import com.orientation.backend.users.domain.model.query.CollaboratorSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.model.valueobjects.Email;
import com.orientation.backend.users.domain.model.valueobjects.FullName;
import com.orientation.backend.users.domain.model.valueobjects.Phone;
import com.orientation.backend.users.domain.repository.CollaboratorRepository;
import com.orientation.backend.users.domain.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public Collaborator createCollaborator(CreateCollaboratorCommand command) {
        List<BusinessViolation> violations = new ArrayList<>();

        Dni dni = Dni.of(command.dni());

        if (collaboratorRepository.existsByDni(dni)) {
            violations.add(new BusinessViolation(
                    "dni",
                    "DNI already exists",
                    ErrorCode.USER_ALREADY_EXISTS
            ));
        }

        if (!violations.isEmpty()) {
            throw new CreatedCollaboratorException(violations);
        }

        Organization organization = null;
        if (command.organizationName() != null) {
            organization = organizationRepository.findByName(command.organizationName())
                    .orElseGet(() -> organizationRepository.save(
                            Organization.builder()
                                    .name(command.organizationName())
                                    .build()
                    ));
        }

        Collaborator collaborator = Collaborator.builder()
                .dni(dni)
                .fullName(FullName.of(command.name(), command.surname()))
                .email(Optional.ofNullable(command.email()).map(Email::of))
                .phone(Optional.ofNullable(command.phone()).map(Phone::of))
                .external(command.external())
                .organization(organization)
                .build();

        return collaboratorRepository.save(collaborator);
    }

    public PageResult<Collaborator> searchCollaborators(CollaboratorSearchCriteria criteria, Pagination pagination) {
        return collaboratorRepository.search(criteria, pagination);
    }
}
