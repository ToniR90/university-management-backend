package com.orientation.backend.users.infrastructure.persistence.mappers;

import com.orientation.backend.users.domain.model.entities.Organization;
import com.orientation.backend.users.infrastructure.persistence.entities.OrganizationJpaEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OrganizationJpaMapper {

    public static Organization toDomain(OrganizationJpaEntity jpaEntity) {
        return Organization.builder()
                .id(jpaEntity.getId())
                .name(jpaEntity.getName())
                .build();
    }

    public static OrganizationJpaEntity toJpaEntity(Organization organization) {
        OrganizationJpaEntity jpaEntity = new OrganizationJpaEntity();
        jpaEntity.setId(organization.getId());
        jpaEntity.setName(organization.getName());
        return jpaEntity;
    }
}