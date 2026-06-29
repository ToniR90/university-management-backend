package com.orientation.backend.users.infrastructure.persistence.specifications;

import com.orientation.backend.users.domain.model.query.CollaboratorSearchCriteria;
import com.orientation.backend.users.infrastructure.persistence.entities.CollaboratorJpaEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class CollaboratorSpecifications {
    private static Specification<CollaboratorJpaEntity> isActive() {
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("active"), true)));
    }

    private static Specification<CollaboratorJpaEntity> nameContains(String name) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
    }

    private static Specification<CollaboratorJpaEntity> hasExternal(Boolean external) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("external"), external);
    }

    public static Specification<CollaboratorJpaEntity> fromCriteria(CollaboratorSearchCriteria criteria) {
        Specification<CollaboratorJpaEntity> specification = isActive();

        if (criteria.getName() != null) {
            specification = specification.and(nameContains(criteria.getName()));
        }

        if (criteria.getExternal() != null) {
            specification = specification.and(hasExternal(criteria.getExternal()));
        }

        return specification;
    }
}
