package com.orientation.backend.sessions.infraestructure.persistance.mappers;

import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.infraestructure.persistance.entities.SessionJpaEntity;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class SessionJpaMapper {

    // ========== JPA -> Domain ==========
    public static Session toDomain(SessionJpaEntity jpaEntity){
        Session.Builder builder = Session.builder();

        builder
                .id(jpaEntity.getId())
                .title(jpaEntity.getTitle())
                .description(jpaEntity.getDescription())
                .motivation(jpaEntity.getMotivation())
                .sessionType(jpaEntity.getSessionType())
                .sessionOrigin(jpaEntity.getSessionOrigin())
                .allWelcome(jpaEntity.isAllWelcome())
                .startDateTime(jpaEntity.getStartDateTime())
                .endDateTime(jpaEntity.getEndDateTime())
                .cancelledAt(jpaEntity.getCancelledAt())
                .cancelledReason(jpaEntity.getCancelReason())
                .infoSentAt(jpaEntity.getInfoSentAt())
                .score(jpaEntity.getScore())
                .summary(jpaEntity.getSummary());

        return builder.build();
    }


    // ========== Domain -> Jpa ==========
    public static SessionJpaEntity toJpaEntity(Session session){
        SessionJpaEntity jpaEntity = new SessionJpaEntity();

        String title = session.getTitle();
        String description = session.getDescription();
        String motivation = session.getMotivation();
        boolean allWelcome = session.isAllWelcome();
        LocalDateTime startDateTime = session.getStartDateTime();
        LocalDateTime endDateTime = session.getEndDateTime();
        LocalDateTime cancelledAt = session.getCancelledAt();
        String cancelledReason = session.getCancelledReason();
        LocalDateTime infoSentAt = session.getInfoSentAt();
        double score = session.getScore() != null ? session.getScore() : 0.0;
        String summary = session.getSummary();


        jpaEntity.setId(session.getId());
        jpaEntity.setTitle(title);
        jpaEntity.setDescription(description);
        jpaEntity.setMotivation(motivation);
        jpaEntity.setSessionType(session.getSessionType());
        jpaEntity.setSessionOrigin(session.getSessionOrigin());
        jpaEntity.setAllWelcome(allWelcome);
        jpaEntity.setStartDateTime(startDateTime);
        jpaEntity.setEndDateTime(endDateTime);
        jpaEntity.setCancelledAt(cancelledAt);
        jpaEntity.setCancelReason(cancelledReason);
        jpaEntity.setInfoSentAt(infoSentAt);
        jpaEntity.setScore(score);
        jpaEntity.setSummary(summary);

        return jpaEntity;
    }
}
