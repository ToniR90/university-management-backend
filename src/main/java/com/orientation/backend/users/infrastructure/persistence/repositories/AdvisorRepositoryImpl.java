package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.domain.model.entities.Advisor;
import com.orientation.backend.users.domain.model.query.AdvisorSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.repository.AdvisorRepository;
import com.orientation.backend.users.infrastructure.persistence.entities.AdvisorJpaEntity;
import com.orientation.backend.users.infrastructure.persistence.mappers.AdvisorJpaMapper;
import com.orientation.backend.users.infrastructure.persistence.specifications.AdvisorSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public PageResult<Advisor> search(AdvisorSearchCriteria criteria, Pagination pagination){

        if (criteria == null){
            throw new IllegalArgumentException("Search criteria cannot be null");
        }

        if (pagination == null){
            throw new IllegalArgumentException("Pagination cannot be null");
        }

        Specification<AdvisorJpaEntity> specification = AdvisorSpecifications.fromCriteria(criteria);

        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getSize());

        Page<AdvisorJpaEntity> page = jpaRepository.findAll(specification, pageRequest);

        List<Advisor> advisors = page.getContent().stream().map(AdvisorJpaMapper::toDomain).toList();

        return new PageResult<>(advisors, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
