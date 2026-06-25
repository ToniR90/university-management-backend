package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.infrastructure.persistence.entities.NoteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface SpringDataNoteRepository extends JpaRepository<NoteJpaEntity, UUID>, JpaSpecificationExecutor<NoteJpaEntity> {
    @Query("SELECT n FROM NoteJpaEntity n " +
            "JOIN PersonJpaEntity p ON n.personId = p.id " +
            "WHERE p.dni = :dni")
    List<NoteJpaEntity> findByPersonDni(@Param("dni") String dni);
}
