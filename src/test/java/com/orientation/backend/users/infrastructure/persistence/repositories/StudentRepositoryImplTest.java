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

import javax.swing.text.html.Option;
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

    @Test
    @DisplayName("Should return empty when not found by ID")
    void shouldReturnEmptyWhenNotFoundById() {
        Optional<Student> found = studentRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should find Student by DNI")
    void shouldFindStudentByDni() {
        Student student = createTestStudent("12345678Z", "test@mail.com");

        Student saved = studentRepository.save(student);

        assertThat(saved).isNotNull();
        assertThat(saved.getDni().getValue()).isEqualTo("12345678Z");
    }

    @Test
    @DisplayName("Should return empty when not found the DNI")
    void shouldReturnEmptyWhenNotFoundByDni() {
        Optional<Student> found = studentRepository.findByDni(Dni.of("12345678Z"));

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should exists by DNI")
    void shouldExistsByDni() {
        Student student = createTestStudent("12345678Z", "test@mail.com");

        studentRepository.save(student);

        boolean exists = studentRepository.existsByDni(Dni.of(student.getDni().getValue()));

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should not exists by DNI")
    void shouldNotExistsByDni() {
        boolean exists = studentRepository.existsByDni(Dni.of("98765432Z"));

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should update Student")
    void shouldUpdateStudent() {
        Student student = createTestStudent("12345678Z", "test@email.com");

        Student saved = studentRepository.save(student);

        saved.updateContactInfo(
                Email.of("newEmail@example.com"),
                saved.getPhone().orElse(null)
        );

        Student updated = studentRepository.save(saved);

        Optional<Student> found = studentRepository.findById(updated.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isPresent();
        assertThat(found.get().getEmail().get().getValue()).isEqualTo("newEmail@example.com");

    }

    @Test
    @DisplayName("Should allow null email")
    void shouldAllowNullEmail() {
        Student student = Student.builder()
                .dni(Dni.of("67890123E"))
                .fullName(FullName.of("Test_name", "Test_FirstSurname", "Test_SecondSurname"))
                .degree("Test Degree")
                .currentYear(CurrentYear.FIRST)
                .alumniInfo(AlumniInfo.notAlumni())
                .rgpdConsent(RgpdConsent.pending())
                .build();

        Student saved = studentRepository.save(student);

        Optional<Student> found = studentRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEmpty();
    }

    @Test
    @DisplayName("Should allow null phone")
    void shouldAllowNullPhone() {
            Student student = Student.builder()
                    .dni(Dni.of("67890123E"))
                    .fullName(FullName.of("Test_name", "Test_FirstSurname", "Test_SecondSurname"))
                    .degree("Test Degree")
                    .currentYear(CurrentYear.FIRST)
                    .alumniInfo(AlumniInfo.notAlumni())
                    .rgpdConsent(RgpdConsent.pending())
                    .build();

            Student saved = studentRepository.save(student);

            Optional<Student> found = studentRepository.findById(saved.getId());
            assertThat(found).isPresent();
            assertThat(found.get().getPhone()).isEmpty();
    }

    @Test
    @DisplayName("Should fail when saving duplicate DNI")
    void shouldFailWhenSavingDuplicateDni() {
        Student student1 = createTestStudent("12345678Z", "test@example.com");
        studentRepository.save(student1);

        Student student2 = createTestStudent("12345678Z", "test2@example.com");

        assertThatThrownBy(() -> studentRepository.save(student2)).isInstanceOf(Exception.class);
    }
}