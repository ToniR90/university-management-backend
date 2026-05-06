package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.infrastructure.persistence.entities.CollaboratorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataCollaboratorRepository extends JpaRepository<CollaboratorJpaEntity, UUID>, JpaSpecificationExecutor<CollaboratorJpaEntity> {
    Optional<CollaboratorJpaEntity> findByDniAndActiveTrue(String dni);
    List<CollaboratorJpaEntity> findAllByActiveTrue();
    boolean existsByDniAndActiveTrue(String dni);

}
