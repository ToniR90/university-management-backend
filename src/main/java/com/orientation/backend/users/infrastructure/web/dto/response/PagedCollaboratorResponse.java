package com.orientation.backend.users.infrastructure.web.dto.response;

import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.users.domain.model.entities.Collaborator;

import java.util.List;

public record PagedCollaboratorResponse (
        List<CollaboratorResponse> content,
        int page,
        int size, long totalElements,
        int totalPages
){

    public static PagedCollaboratorResponse fromDomain(PageResult<Collaborator> result) {
        return new PagedCollaboratorResponse(
                result.getContent().stream().map(CollaboratorResponse::fromDomain).toList(),
                result.getPage(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}
