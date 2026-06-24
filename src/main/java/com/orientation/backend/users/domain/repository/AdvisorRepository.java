package com.orientation.backend.users.domain.repository;

import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.domain.model.entities.Advisor;
import com.orientation.backend.users.domain.model.query.AdvisorSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.Dni;

import java.util.List;
import java.util.Optional;

public interface AdvisorRepository {
    Advisor save(Advisor advisor);
    Optional<Advisor> findByDni(Dni dni);
    boolean existsByDni(Dni dni);
    List<Advisor> findAll();
    PageResult<Advisor> search(AdvisorSearchCriteria criteria, Pagination pagination);
}
