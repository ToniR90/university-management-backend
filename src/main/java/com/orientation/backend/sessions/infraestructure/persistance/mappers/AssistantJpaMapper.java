package com.orientation.backend.sessions.infraestructure.persistance.mappers;

import com.orientation.backend.sessions.domain.model.entities.Assistant;
import com.orientation.backend.sessions.infraestructure.persistance.entities.AssistantId;
import com.orientation.backend.sessions.infraestructure.persistance.entities.AssistantJpaEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AssistantJpaMapper {

    // ========== JPA -> Domain ==========
    public static Assistant toDomain(AssistantJpaEntity jpaEntity) {
        Assistant.Builder builder = Assistant.builder();

        builder
                .sessionId(jpaEntity.getId().getSessionId())
                .personId(jpaEntity.getId().getPersonId())
                .registered(jpaEntity.isRegistered())
                .attended(jpaEntity.isAttended());

        return builder.build();
    }


    // ========== Domain -> Jpa ==========
    public static AssistantJpaEntity toJpaEntity(Assistant assistant) {
        AssistantJpaEntity jpaEntity = new AssistantJpaEntity();
        AssistantId id = new AssistantId(assistant.getSessionId(), assistant.getPersonId());

        jpaEntity.setId(id);
        jpaEntity.setRegistered(assistant.isRegistered());
        jpaEntity.setAttended(assistant.isAttended());

        return jpaEntity;
    }
}
