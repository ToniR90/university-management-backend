package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.infrastructure.persistence.entities.StudentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataStudentRepository extends JpaRepository<StudentJpaEntity, Long> {
    Optional<StudentJpaEntity> findByIdAndActiveTrue(Long id);
    Optional<StudentJpaEntity> findByDniAndActiveTrue(String dni);
    List<StudentJpaEntity> findAllByActiveTrue();
    boolean existsByDniAndActiveTrue(String dni);
}