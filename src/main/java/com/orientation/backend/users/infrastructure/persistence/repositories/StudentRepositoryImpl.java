package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.query.PageResult;
import com.orientation.backend.users.domain.model.query.Pagination;
import com.orientation.backend.users.domain.model.query.StudentSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.repository.StudentRepository;
import com.orientation.backend.users.infrastructure.persistence.entities.StudentJpaEntity;
import com.orientation.backend.users.infrastructure.persistence.mappers.StudentJpaMapper;
import com.orientation.backend.users.infrastructure.persistence.specifications.StudentSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final SpringDataStudentRepository jpaRepository;

    public StudentRepositoryImpl(SpringDataStudentRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Student save(Student student) {
        StudentJpaEntity jpaEntity = StudentJpaMapper.toJpaEntity(student);

        StudentJpaEntity saved = jpaRepository.save(jpaEntity);

        return StudentJpaMapper.toDomain(saved);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return jpaRepository.findByIdAndActiveTrue(id).map(StudentJpaMapper::toDomain);
    }

    @Override
    public Optional<Student> findByDni(Dni dni) {
        return jpaRepository.findByDniAndActiveTrue(dni.getValue()).map(StudentJpaMapper::toDomain);
    }

    @Override
    public boolean existsByDni(Dni dni) {
        return jpaRepository.existsByDniAndActiveTrue(dni.getValue());
    }

    @Override
    public List<Student> findAll() {
        return jpaRepository.findAllByActiveTrue()
                .stream()
                .map(StudentJpaMapper::toDomain)
                .toList();
    }

    @Override
    public PageResult<Student> search(StudentSearchCriteria criteria, Pagination pagination) {

        if (criteria == null) {
            throw new IllegalArgumentException("Search criteria cannot be null");
        }
        if (pagination == null) {
            throw new IllegalArgumentException("Pagination cannot be null");
        }

        Specification<StudentJpaEntity> specification = StudentSpecifications.fromCriteria(criteria);

        PageRequest pageRequest = PageRequest.of(pagination.getPage(), pagination.getSize());

        Page<StudentJpaEntity> page = jpaRepository.findAll(specification, pageRequest);

        List<Student> students = page.getContent().stream().map(StudentJpaMapper::toDomain).toList();

        return new PageResult<>(students, page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
