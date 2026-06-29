package com.orientation.backend.sessions.infraestructure.persistance.mappers;

import com.orientation.backend.sessions.domain.model.entities.AdvisorInSession;
import com.orientation.backend.sessions.infraestructure.persistance.entities.AdvisorInSessionId;
import com.orientation.backend.sessions.infraestructure.persistance.entities.AdvisorInSessionJpaEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AdvisorInSessionJpaMapper {

    // ========== JPA -> Domain ==========
    public static AdvisorInSession toDomain(AdvisorInSessionJpaEntity jpaEntity) {
        return AdvisorInSession.builder()
                .sessionId(jpaEntity.getId().getSessionId())
                .advisorId(jpaEntity.getId().getAdvisorId())
                .build();
    }

    // ========== Domain -> Jpa ==========
    public static AdvisorInSessionJpaEntity toJpaEntity(AdvisorInSession advisorInSession) {
        AdvisorInSessionJpaEntity jpaEntity = new AdvisorInSessionJpaEntity();
        AdvisorInSessionId id = new AdvisorInSessionId(advisorInSession.getSessionId(), advisorInSession.getAdvisorId());

        jpaEntity.setId(id);

        return jpaEntity;
    }
}