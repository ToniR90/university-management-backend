package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.infrastructure.persistence.entities.StudentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataStudentRepository extends JpaRepository<StudentJpaEntity, Long> {
    Optional<StudentJpaEntity> findByDni(String dni);
    boolean existsByDni(String dni);
}