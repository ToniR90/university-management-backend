package com.orientation.backend.users.domain.repository;

import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.domain.model.entities.Collaborator;
import com.orientation.backend.users.domain.model.query.CollaboratorSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.Dni;

import java.util.List;
import java.util.Optional;

public interface CollaboratorRepository {
    Collaborator save(Collaborator collaborator);
    Optional<Collaborator> findByDni(Dni dni);
    boolean existsByDni(Dni dni);
    List<Collaborator> findAll();
    PageResult<Collaborator> search(CollaboratorSearchCriteria criteria, Pagination pagination);
}
