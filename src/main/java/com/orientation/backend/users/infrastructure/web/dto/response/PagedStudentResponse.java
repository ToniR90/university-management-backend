package com.orientation.backend.users.infrastructure.web.dto.response;

import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.shared.domain.model.query.PageResult;

import java.util.List;

public record PagedStudentResponse (
    List<StudentResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages
    ) {

    public static PagedStudentResponse fromDomain(PageResult<Student> result) {
        return new PagedStudentResponse(
                result.getContent().stream().map(StudentResponse::fromDomain).toList(),
                result.getPage(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}
