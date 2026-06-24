package com.orientation.backend.users.domain.repository;

import com.orientation.backend.users.domain.model.entities.Organization;

import java.util.Optional;

public interface OrganizationRepository {
    Organization save(Organization organization);
    Optional<Organization> findByName(String name);
}