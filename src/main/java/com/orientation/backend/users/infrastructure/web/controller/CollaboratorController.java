package com.orientation.backend.users.infrastructure.web.controller;

import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.application.commands.collaborators.CreateCollaboratorCommand;
import com.orientation.backend.users.application.services.CollaboratorService;
import com.orientation.backend.users.domain.model.entities.Collaborator;
import com.orientation.backend.users.domain.model.query.CollaboratorSearchCriteria;
import com.orientation.backend.users.infrastructure.web.dto.request.CreateCollaboratorRequest;
import com.orientation.backend.users.infrastructure.web.dto.response.CollaboratorResponse;
import com.orientation.backend.users.infrastructure.web.dto.response.PagedCollaboratorResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/collaborators")
@RequiredArgsConstructor
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    @PostMapping
    public ResponseEntity<CollaboratorResponse> createCollaborator(@Valid @RequestBody CreateCollaboratorRequest request) {
        CreateCollaboratorCommand command = new CreateCollaboratorCommand(
                request.dni(),
                request.name(),
                request.surname(),
                request.email(),
                request.phone(),
                request.external(),
                request.organizationName()
        );

        Collaborator collaborator = collaboratorService.createCollaborator(command);
        CollaboratorResponse response = CollaboratorResponse.fromDomain(collaborator);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PagedCollaboratorResponse> getAllCollaborators(@RequestParam (defaultValue = "0") int page,
                                                                         @RequestParam (defaultValue = "20") int size,
                                                                         @RequestParam (required = false) String name,
                                                                         @RequestParam (required = false) Boolean external) {
        Pagination pagination = new Pagination(page, size);
        CollaboratorSearchCriteria criteria = new CollaboratorSearchCriteria(name, external);
        PageResult<Collaborator> collaborators = collaboratorService.searchCollaborators(criteria, pagination);

        return ResponseEntity.ok(PagedCollaboratorResponse.fromDomain(collaborators));
    }
}
