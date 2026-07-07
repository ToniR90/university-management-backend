package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.infrastructure.persistence.entities.AdvisorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataAdvisorRepository extends JpaRepository<AdvisorJpaEntity, UUID>, JpaSpecificationExecutor<AdvisorJpaEntity> {
    Optional<AdvisorJpaEntity> findByDniAndActiveTrue(String dni);
    List<AdvisorJpaEntity> findAllByActiveTrue();
    boolean existsByDniAndActiveTrue(String dni);

    @Query(value = "SELECT a.id FROM advisor a JOIN person p ON a.person_id = p.id WHERE p.dni = :dni AND p.active = true", nativeQuery = true)
    Optional<UUID> findAdvisorIdByDni(@Param("dni") String dni);
}