package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.domain.model.entities.Collaborator;
import com.orientation.backend.users.domain.model.query.CollaboratorSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.repository.CollaboratorRepository;
import com.orientation.backend.users.infrastructure.persistence.entities.CollaboratorJpaEntity;
import com.orientation.backend.users.infrastructure.persistence.mappers.CollaboratorJpaMapper;
import com.orientation.backend.users.infrastructure.persistence.specifications.CollaboratorSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public PageResult<Collaborator> search(CollaboratorSearchCriteria criteria, Pagination pagination) {

        if (criteria == null) {
            throw new IllegalArgumentException("Search criteria cannot be null");
        }

        if (pagination == null) {
            throw new IllegalArgumentException("Pagination cannot be null");
        }

        Specification<CollaboratorJpaEntity> specification = CollaboratorSpecifications.fromCriteria(criteria);

        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getSize());

        Page<CollaboratorJpaEntity> page = jpaRepository.findAll(specification, pageRequest);

        List<Collaborator> collaborators = page.getContent().stream().map(CollaboratorJpaMapper::toDomain).toList();

        return new PageResult<>(collaborators, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
