package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.repository.StudentRepository;
import com.orientation.backend.users.infrastructure.persistence.entities.StudentJpaEntity;
import com.orientation.backend.users.infrastructure.persistence.mappers.StudentJpaMapper;
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
        return jpaRepository.findById(id).map(StudentJpaMapper::toDomain);
    }

    @Override
    public Optional<Student> findByDni(Dni dni) {
        return jpaRepository.findByDni(dni.getValue()).map(StudentJpaMapper::toDomain);
    }

    @Override
    public boolean existsByDni(Dni dni) {
        return jpaRepository.existsByDni(dni.getValue());
    }

    @Override
    public List<Student> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(StudentJpaMapper::toDomain)
                .toList();
    }
}
