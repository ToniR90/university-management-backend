package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.infrastructure.persistence.entities.StudentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataStudentRepository extends JpaRepository<StudentJpaEntity, UUID>, JpaSpecificationExecutor<StudentJpaEntity> {
    Optional<StudentJpaEntity> findByDniAndActiveTrue(String dni);
    List<StudentJpaEntity> findAllByActiveTrue();
    boolean existsByDniAndActiveTrue(String dni);
}