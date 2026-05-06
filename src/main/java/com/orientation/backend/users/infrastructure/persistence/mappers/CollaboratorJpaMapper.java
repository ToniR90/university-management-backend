package com.orientation.backend.users.infrastructure.persistence.mappers;

import com.orientation.backend.users.domain.model.entities.Collaborator;
import com.orientation.backend.users.domain.model.entities.Organization;
import com.orientation.backend.users.infrastructure.persistence.entities.CollaboratorJpaEntity;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class CollaboratorJpaMapper {

    // ========== JPA -> Domain ==========
    public static Collaborator toDomain(CollaboratorJpaEntity jpaEntity){
        Collaborator.Builder builder = Collaborator.builder();
        PersonJpaMapper.fillBuilder(jpaEntity, builder);

        boolean external = jpaEntity.isExternal();
        Organization organization = null;
        if(jpaEntity.getOrganizationId() != null) {
            organization = Organization.builder()
                    .id(jpaEntity.getOrganizationId())
                    .build();
        }

        builder
                .external(external)
                .organization(organization);

        return builder.build();
    }

    // ========== Domain -> Jpa ==========
    public static CollaboratorJpaEntity toJpaEntity(Collaborator collaborator){
        CollaboratorJpaEntity jpaEntity = new CollaboratorJpaEntity();
        PersonJpaMapper.fillJpaEntity(collaborator, jpaEntity);

        boolean external = collaborator.isExternal();

        UUID organizationId = collaborator.getOrganization() != null
                ? collaborator.getOrganization().getId()
                : null;

        jpaEntity.setExternal(external);
        jpaEntity.setOrganizationId(organizationId);

        return jpaEntity;
    }
}
