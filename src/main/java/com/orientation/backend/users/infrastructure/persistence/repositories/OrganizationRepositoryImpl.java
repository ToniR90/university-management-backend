package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.domain.model.entities.Organization;
import com.orientation.backend.users.domain.repository.OrganizationRepository;
import com.orientation.backend.users.infrastructure.persistence.entities.OrganizationJpaEntity;
import com.orientation.backend.users.infrastructure.persistence.mappers.OrganizationJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrganizationRepositoryImpl implements OrganizationRepository {

    private final SpringDataOrganizationRepository jpaRepository;

    @Override
    public Organization save(Organization organization){
        OrganizationJpaEntity jpaEntity = OrganizationJpaMapper.toJpaEntity(organization);

        OrganizationJpaEntity saved = jpaRepository.save(jpaEntity);

        return OrganizationJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Organization> findByName(String name){
        return jpaRepository.findByName(name).map(OrganizationJpaMapper::toDomain);
    }
}