package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.domain.model.entities.Advisor;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.repository.AdvisorRepository;
import com.orientation.backend.users.infrastructure.persistence.entities.AdvisorJpaEntity;
import com.orientation.backend.users.infrastructure.persistence.mappers.AdvisorJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdvisorRepositoryImpl implements AdvisorRepository {

    private final SpringDataAdvisorRepository jpaRepository;

    @Override
    public Advisor save(Advisor advisor) {
        AdvisorJpaEntity jpaEntity = AdvisorJpaMapper.toJpaEntity(advisor);

        AdvisorJpaEntity saved = jpaRepository.save(jpaEntity);

        return AdvisorJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Advisor> findByDni(Dni dni) {
        return jpaRepository.findByDniAndActiveTrue(dni.getValue()).map(AdvisorJpaMapper::toDomain);
    }

    @Override
    public boolean existsByDni(Dni dni) {
        return jpaRepository.existsByDniAndActiveTrue(dni.getValue());
    }

    @Override
    public List<Advisor> findAll() {
        return jpaRepository.findAllByActiveTrue()
                .stream()
                .map(AdvisorJpaMapper::toDomain)
                .toList();
    }
}
