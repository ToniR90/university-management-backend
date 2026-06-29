package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.infrastructure.persistence.entities.OrganizationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface SpringDataOrganizationRepository extends JpaRepository<OrganizationJpaEntity, UUID> {
    Optional<OrganizationJpaEntity> findByName(String name);
}