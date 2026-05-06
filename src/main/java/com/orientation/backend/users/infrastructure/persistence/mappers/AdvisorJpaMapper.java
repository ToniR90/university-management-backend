package com.orientation.backend.users.infrastructure.persistence.mappers;

import com.orientation.backend.users.domain.model.entities.Advisor;
import com.orientation.backend.users.infrastructure.persistence.entities.AdvisorJpaEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AdvisorJpaMapper {

    // ========== JPA -> Domain ==========
    public static Advisor toDomain(AdvisorJpaEntity jpaEntity) {
        Advisor.Builder builder = Advisor.builder();
        PersonJpaMapper.fillBuilder(jpaEntity, builder);

        return builder.build();
    }

    // ========== Domain -> Jpa ==========
    public static AdvisorJpaEntity toJpaEntity(Advisor advisor) {
        AdvisorJpaEntity jpaEntity = new AdvisorJpaEntity();
        PersonJpaMapper.fillJpaEntity(advisor, jpaEntity);

        return jpaEntity;
    }
}
