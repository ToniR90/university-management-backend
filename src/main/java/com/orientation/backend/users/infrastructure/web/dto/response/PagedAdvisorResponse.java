package com.orientation.backend.users.infrastructure.web.dto.response;

import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.users.domain.model.entities.Advisor;

import java.util.List;

public record PagedAdvisorResponse (
        List<AdvisorResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages
){

    public static PagedAdvisorResponse fromDomain(PageResult<Advisor> result) {
        return new PagedAdvisorResponse(
                result.getContent().stream().map(AdvisorResponse::fromDomain).toList(),
                result.getPage(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}
