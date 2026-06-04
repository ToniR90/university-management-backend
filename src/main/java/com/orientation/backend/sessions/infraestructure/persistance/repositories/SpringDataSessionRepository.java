package com.orientation.backend.sessions.infraestructure.persistance.repositories;

import com.orientation.backend.sessions.infraestructure.persistance.entities.SessionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataSessionRepository extends JpaRepository<SessionJpaEntity, UUID>, JpaSpecificationExecutor<SessionJpaEntity> {
    @Query("SELECT s FROM SessionJpaEntity s " +
            "WHERE s.cancelledAt IS NULL " +
            "AND s.startDateTime > CURRENT_TIMESTAMP " +
            "AND (:title IS NULL OR LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    List<SessionJpaEntity> findCancellable(@Param("title") String title);

}
