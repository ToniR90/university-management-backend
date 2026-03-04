package com.orientation.backend.users.domain.repository;

import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.query.PageResult;
import com.orientation.backend.users.domain.model.query.Pagination;
import com.orientation.backend.users.domain.model.query.StudentSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.Dni;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Student save(Student student);
    Optional<Student> findById(Long id);
    Optional<Student> findByDni(Dni dni);
    boolean existsByDni(Dni dni);
    List<Student> findAll();
    PageResult<Student> search(StudentSearchCriteria criteria, Pagination pagination);
}
