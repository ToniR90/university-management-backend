package com.orientation.backend.sessions.infraestructure.persistance.repositories;

import com.orientation.backend.sessions.domain.model.entities.Assistant;
import com.orientation.backend.sessions.domain.repository.AssistantRepository;
import com.orientation.backend.sessions.infraestructure.persistance.entities.AssistantJpaEntity;
import com.orientation.backend.sessions.infraestructure.persistance.mappers.AssistantJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AssistantRepositoryImpl implements AssistantRepository {

    private final SpringDataAssistantRepository jpaRepository;

    @Override
    public Assistant add(Assistant assistant) {
        AssistantJpaEntity jpaEntity = AssistantJpaMapper.toJpaEntity(assistant);
        AssistantJpaEntity added = jpaRepository.save(jpaEntity);
        return AssistantJpaMapper.toDomain(added);
    }

    @Override
    public void remove(UUID sessionId, UUID personId) {
        jpaRepository.deleteByIdSessionIdAndIdPersonId(sessionId, personId);
    }

    @Override
    public List<Assistant> findBySessionId(UUID sessionId) {
        return jpaRepository.findByIdSessionId(sessionId)
                .stream()
                .map(AssistantJpaMapper::toDomain)
                .toList();
    }
}
