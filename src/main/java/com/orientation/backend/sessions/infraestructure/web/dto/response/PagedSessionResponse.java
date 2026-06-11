package com.orientation.backend.sessions.infraestructure.web.dto.response;

import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.shared.domain.model.query.PageResult;

import java.util.List;

public record PagedSessionResponse (
        List<SessionResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public static PagedSessionResponse fromDomain(PageResult<Session> result) {
        return new PagedSessionResponse(
                result.getContent().stream().map(SessionResponse::fromDomain).toList(),
                result.getPage(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}
