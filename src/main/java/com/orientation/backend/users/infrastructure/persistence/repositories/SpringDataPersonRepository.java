package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.infrastructure.persistence.entities.PersonJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataPersonRepository extends JpaRepository<PersonJpaEntity, UUID> {
    Optional<PersonJpaEntity> findByDniAndActiveTrue(String dni);
}