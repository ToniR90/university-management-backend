package com.orientation.backend.users.infrastructure.persistence.specifications;

import com.orientation.backend.users.domain.model.query.AdvisorSearchCriteria;
import com.orientation.backend.users.infrastructure.persistence.entities.AdvisorJpaEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

/**
 * This class includes all the methods for the different specifications of the filters
 */
@UtilityClass
public class AdvisorSpecifications {
    private static Specification<AdvisorJpaEntity> isActive() {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("active"), true));
    }

    private static Specification<AdvisorJpaEntity> hasDni(String dni) {
        return((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("dni"), dni));
    }

    private static Specification<AdvisorJpaEntity> nameContains(String name) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
    }


    public static Specification<AdvisorJpaEntity> fromCriteria(AdvisorSearchCriteria criteria){

        Specification<AdvisorJpaEntity> specification = isActive();

        if(criteria.getName() != null) {
            specification = specification.and(AdvisorSpecifications.nameContains(criteria.getName()));
        }

        if(criteria.getDni() != null) {
            specification = specification.and(AdvisorSpecifications.hasDni(criteria.getDni()));
        }

        return specification;
    }
}
