package com.orientation.backend.sessions.infraestructure.persistance.repositories;

import com.orientation.backend.sessions.infraestructure.persistance.entities.AssistantId;
import com.orientation.backend.sessions.infraestructure.persistance.entities.AssistantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface SpringDataAssistantRepository extends JpaRepository<AssistantJpaEntity, AssistantId>, JpaSpecificationExecutor<AssistantJpaEntity> {
    List<AssistantJpaEntity> findByIdSessionId(UUID sessionId);
    void deleteByIdSessionIdAndIdPersonId(UUID sessionId, UUID personId);
}
