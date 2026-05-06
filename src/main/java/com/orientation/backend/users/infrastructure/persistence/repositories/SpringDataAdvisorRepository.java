package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.infrastructure.persistence.entities.AdvisorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataAdvisorRepository extends JpaRepository<AdvisorJpaEntity, UUID>, JpaSpecificationExecutor<AdvisorJpaEntity> {
    Optional<AdvisorJpaEntity> findByDniAndActiveTrue(String dni);
    List<AdvisorJpaEntity> findAllByActiveTrue();
    boolean existsByDniAndActiveTrue(String dni);
}