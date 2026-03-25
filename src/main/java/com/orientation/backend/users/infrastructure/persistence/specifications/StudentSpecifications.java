package com.orientation.backend.users.infrastructure.persistence.specifications;

import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.Degree;
import com.orientation.backend.users.domain.model.query.StudentSearchCriteria;
import com.orientation.backend.users.infrastructure.persistence.entities.StudentJpaEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * This class includes all the methods for the diferents specifications of the filters
 */
public final class StudentSpecifications {

    private StudentSpecifications() {}

    private static Specification<StudentJpaEntity> isActive() {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("active"), true));
    }

    private static Specification<StudentJpaEntity> hasDni(String dni) {
        return((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("dni"), dni));
    }

    private static Specification<StudentJpaEntity> hasCurrentYear(CurrentYear year) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("currentYear"), year.name()));
    }

    private static Specification<StudentJpaEntity> hasDegree(Degree degree) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("degree"), degree.name()));
    }

    private static Specification<StudentJpaEntity> isAlumni(Boolean isAlumni) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isAlumni"), isAlumni));
    }

    private static Specification<StudentJpaEntity> nameContains(String name) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
    }


    public static Specification<StudentJpaEntity> fromCriteria(StudentSearchCriteria criteria){

        Specification<StudentJpaEntity> specification = isActive();

        if(criteria.getName() != null) {
            specification = specification.and(StudentSpecifications.nameContains(criteria.getName()));
        }

        if(criteria.getDni() != null) {
            specification = specification.and(StudentSpecifications.hasDni(criteria.getDni()));
        }

        if(criteria.getCurrentYear() != null) {
            specification = specification.and(StudentSpecifications.hasCurrentYear(criteria.getCurrentYear()));
        }

        if (criteria.getDegree() != null) {
            specification = specification.and(StudentSpecifications.hasDegree(criteria.getDegree()));
        }

        if ((criteria.getIsAlumni() != null)) {
            specification = specification.and(StudentSpecifications.isAlumni(criteria.getIsAlumni()));
        }

        return specification;
    }
}
