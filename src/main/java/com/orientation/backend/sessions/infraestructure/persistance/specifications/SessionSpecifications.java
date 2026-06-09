package com.orientation.backend.sessions.infraestructure.persistance.specifications;

import com.orientation.backend.sessions.domain.model.query.SessionSearchCriteria;
import com.orientation.backend.sessions.infraestructure.persistance.entities.SessionJpaEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

@UtilityClass
public class SessionSpecifications {
    public static Specification<SessionJpaEntity> isCancellable() {
        return (root, query, cb) ->
                cb.and(
                        cb.isNull(root.get("cancelledAt")),
                        cb.greaterThan(root.get("startDateTime"), LocalDateTime.now())
                );
    }

    public static Specification<SessionJpaEntity> hasTitle(String title) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                );
    }

    public static Specification<SessionJpaEntity> fromCriteria(SessionSearchCriteria criteria) {
        Specification<SessionJpaEntity> specification = Specification.where(null);

        if (criteria.getTitle() != null) {
            specification = specification.and(hasTitle(criteria.getTitle()));
        }

        return specification;
    }
}
