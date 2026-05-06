package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.domain.model.entities.Collaborator;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.repository.CollaboratorRepository;
import com.orientation.backend.users.infrastructure.persistence.entities.CollaboratorJpaEntity;
import com.orientation.backend.users.infrastructure.persistence.mappers.CollaboratorJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CollaboratorRepositoryImpl implements CollaboratorRepository {

    private final SpringDataCollaboratorRepository jpaRepository;

    @Override
    public Collaborator save(Collaborator collaborator) {
        CollaboratorJpaEntity jpaEntity = CollaboratorJpaMapper.toJpaEntity(collaborator);

        CollaboratorJpaEntity saved = jpaRepository.save(jpaEntity);

        return CollaboratorJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Collaborator> findByDni(Dni dni) {
        return jpaRepository.findByDniAndActiveTrue(dni.getValue()).map(CollaboratorJpaMapper::toDomain);
    }

    @Override
    public boolean existsByDni(Dni dni) {
        return jpaRepository.existsByDniAndActiveTrue(dni.getValue());
    }

    @Override
    public List<Collaborator> findAll() {
        return jpaRepository.findAllByActiveTrue()
                .stream()
                .map(CollaboratorJpaMapper::toDomain)
                .toList();
    }
}
