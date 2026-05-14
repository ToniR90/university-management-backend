package com.orientation.backend.sessions.infraestructure.persistance.repositories;

import com.orientation.backend.sessions.infraestructure.persistance.entities.SessionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SpringDataSessionRepository extends JpaRepository<SessionJpaEntity, UUID>, JpaSpecificationExecutor<SessionJpaEntity> {
}
