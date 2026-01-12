package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.valueobjects.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(StudentRepositoryImpl.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/students_test",
        "spring.datasource.username=postgres",
        "spring.datasource.password=postgres",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
class StudentRepositoryImplTest {

    @Autowired
    private StudentRepositoryImpl studentRepository;

    private Student createTestStudent(String dni, String email) {
        return Student.builder()
                .dni(Dni.of(dni))
                .fullName(FullName.of("Test", "Student", "Surname"))
                .email(Optional.of(Email.of(email)))
                .phone(Optional.of(Phone.of("+34612345678")))
                .degree("Ingeniería Informática")
                .currentYear(CurrentYear.FIRST)
                .alumniInfo(AlumniInfo.notAlumni())
                .rgpdConsent(RgpdConsent.pending())
                .build();
    }

    @Test
    @DisplayName("Should save student correctly")
    void shouldSaveStudent() {
        Student student = createTestStudent("12345678Z", "test@example.com");

        Student saved = studentRepository.save(student);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDni().getValue()).isEqualTo("12345678Z");
        assertThat(saved.getEmail()).isPresent();
        assertThat(saved.getEmail().get().getValue()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should find Student by ID")
    void shouldFindStudentById() {
        Student student = createTestStudent("12345678Z", "test@email.com");

        Student saved = studentRepository.save(student);

        Optional<Student> found = studentRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getDni().getValue()).isEqualTo("12345678Z");
    }
}