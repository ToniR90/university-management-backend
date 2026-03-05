package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.AlumniType;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.query.PageResult;
import com.orientation.backend.users.domain.model.query.Pagination;
import com.orientation.backend.users.domain.model.query.StudentSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
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
        "spring.flyway.enabled=false",
        "spring.sql.init.mode=always",
        "spring.jpa.defer-datasource-initialization=true"
})
class StudentRepositoryImplTest {

    @Autowired
    private StudentRepositoryImpl studentRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Student createTestStudent(String dni, String email) {
        return Student.builder()
                .dni(Dni.of(dni))
                .fullName(FullName.of("Test_Name", "Test_Surname1", "Test_Surname2"))
                .email(Optional.of(Email.of(email)))
                .phone(Optional.of(Phone.of("+34612345678")))
                .degree("Videojocs")
                .currentYear(CurrentYear.FIRST)
                .alumniInfo(AlumniInfo.notAlumni())
                .rgpdConsent(RgpdConsent.pending())
                .build();
    }

    private Student createStudentWithDetails(String dni, String name, String surname,
                                             String degree, CurrentYear year, boolean alumni) {
        return Student.builder()
                .dni(Dni.of(dni))
                .fullName(FullName.of(name, surname, null))
                .degree(degree)
                .currentYear(year)
                .alumniInfo(alumni ? AlumniInfo.createAlumni(AlumniType.BACHELOR, 2023) : AlumniInfo.notAlumni())
                .rgpdConsent(RgpdConsent.pending())
                .build();
    }

    // ========== CRUD Tests ==========

    @Test
    @DisplayName("Should save student correctly")
    void shouldSaveStudent() {
        Student student = createTestStudent("00000000T", "test@example.com");

        Student saved = studentRepository.save(student);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDni().getValue()).isEqualTo("00000000T");
        assertThat(saved.getEmail()).isPresent();
        assertThat(saved.getEmail().get().getValue()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should find Student by ID")
    void shouldFindStudentById() {
        Student student = createTestStudent("00000001R", "test@email.com");

        Student saved = studentRepository.save(student);

        Optional<Student> found = studentRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getDni().getValue()).isEqualTo("00000001R");
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
        Student student = createTestStudent("00000002W", "test@mail.com");

        Student saved = studentRepository.save(student);

        assertThat(saved).isNotNull();
        assertThat(saved.getDni().getValue()).isEqualTo("00000002W");
    }

    @Test
    @DisplayName("Should return empty when not found the DNI")
    void shouldReturnEmptyWhenNotFoundByDni() {
        Optional<Student> found = studentRepository.findByDni(Dni.of("00000003A"));

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should exists by DNI")
    void shouldExistsByDni() {
        Student student = createTestStudent("00000004G", "test@mail.com");

        studentRepository.save(student);

        boolean exists = studentRepository.existsByDni(Dni.of(student.getDni().getValue()));

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should not exists by DNI")
    void shouldNotExistsByDni() {
        boolean exists = studentRepository.existsByDni(Dni.of("00000005M"));

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should update Student")
    void shouldUpdateStudent() {
        Student student = createTestStudent("00000006Y", "test@email.com");

        Student saved = studentRepository.save(student);

        saved.updateContactInfo(
                Email.of("new_email@example.com"),
                saved.getPhone().orElse(null)
        );

        Student updated = studentRepository.save(saved);

        Optional<Student> found = studentRepository.findById(updated.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isPresent();
        assertThat(found.get().getEmail().get().getValue()).isEqualTo("new_email@example.com");
    }

    @Test
    @DisplayName("Should allow null email")
    void shouldAllowNullEmail() {
        Student student = Student.builder()
                .dni(Dni.of("00000007F"))
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
                .dni(Dni.of("00000008P"))
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
        Student student1 = createTestStudent("00000009D", "test@example.com");
        studentRepository.save(student1);

        Student student2 = createTestStudent("00000009D", "test2@example.com");

        assertThatThrownBy(() -> studentRepository.save(student2)).isInstanceOf(Exception.class);
    }

    // ========== Soft Delete Tests ==========

    @Test
    @DisplayName("Should not find student by ID after deactivation")
    void shouldNotFindByIdAfterDeactivation() {
        Student student = createTestStudent("00000010X", "test@example.com");
        Student saved = studentRepository.save(student);

        saved.deactivate();
        studentRepository.save(saved);

        assertThat(studentRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should not find student by DNI after deactivation")
    void shouldNotFindByDniAfterDeactivation() {
        Student student = createTestStudent("00000011B", "test@example.com");
        Student saved = studentRepository.save(student);

        saved.deactivate();
        studentRepository.save(saved);

        assertThat(studentRepository.findByDni(Dni.of("00000011B"))).isEmpty();
    }

    @Test
    @DisplayName("Should not include deactivated students in findAll")
    void shouldNotIncludeDeactivatedInFindAll() {
        Student active = createTestStudent("00000012N", "active@example.com");
        Student toDeactivate = createTestStudent("00000013J", "inactive@example.com");

        studentRepository.save(active);
        Student saved = studentRepository.save(toDeactivate);

        saved.deactivate();
        studentRepository.save(saved);

        List<Student> all = studentRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getDni().getValue()).isEqualTo("00000012N");
    }

    @Test
    @DisplayName("Should not exist by DNI after deactivation")
    void shouldNotExistByDniAfterDeactivation() {
        Student student = createTestStudent("00000014Z", "test@example.com");
        Student saved = studentRepository.save(student);

        saved.deactivate();
        studentRepository.save(saved);

        assertThat(studentRepository.existsByDni(Dni.of("00000014Z"))).isFalse();
    }

    @Test
    @DisplayName("Should reuse DNI after deactivation")
    void shouldReuseDniAfterDeactivation() {
        Student student = createTestStudent("00000015S", "test@example.com");
        Student saved = studentRepository.save(student);

        saved.deactivate();
        studentRepository.save(saved);

        entityManager.flush();
        entityManager.clear();

        Student newStudent = createTestStudent("00000015S", "new@example.com");
        Student newSaved = studentRepository.save(newStudent);

        assertThat(newSaved.getId()).isNotNull();
        assertThat(studentRepository.findByDni(Dni.of("00000015S"))).isPresent();
    }


    // ========== SEARCH TESTS ==========

    @Test
    @DisplayName("Should search all Students if no parameters are given")
    void shouldReturnAllStudentsIfNoParametersAreGiven() {
        Student student = createStudentWithDetails("00000016Q", "Student", "Surname", "Maths",
                CurrentYear.FIRST, false);
        Student student1 = createStudentWithDetails("00000017V", "Student1", "Surname1", "Teacher",
                CurrentYear.SECOND, true);
        Student student2 = createStudentWithDetails("00000018H", "Student2", "Surname2", "Developer",
                CurrentYear.FIFTH, false);

        Student saved = studentRepository.save(student);
        Student saved1 = studentRepository.save(student1);
        Student saved2 = studentRepository.save(student2);

        StudentSearchCriteria criteria = new StudentSearchCriteria(null, null, null, null);
        Pagination pagination = new Pagination(0, 20);
        PageResult<Student> result = studentRepository.search(criteria, pagination);

        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getPage()).isZero();
        assertThat(result.getSize()).isEqualTo(20);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should search students with filters")
    void shouldSearchStudentsWithFilters() {
        Student student = createStudentWithDetails("00000019L", "Student", "Surname", "Maths",
                CurrentYear.FIRST, false);
        Student student1 = createStudentWithDetails("00000020C", "Student1", "Surname1", "Teacher",
                CurrentYear.SECOND, true);
        Student student2 = createStudentWithDetails("00000021K", "Student2", "Surname2", "Developer",
                CurrentYear.FIFTH, false);

        Student saved = studentRepository.save(student);
        Student saved1 = studentRepository.save(student1);
        Student saved2 = studentRepository.save(student2);

        StudentSearchCriteria criteria = new StudentSearchCriteria("Student", null, null, true);
        Pagination pagination = new Pagination(0, 20);
        PageResult<Student> result = studentRepository.search(criteria, pagination);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getPage()).isZero();
        assertThat(result.getSize()).isEqualTo(20);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent().get(0).getDni().getValue()).isEqualTo("00000020C");
    }

    @Test
    @DisplayName("Should search students with filters and case-insensitive")
    void shouldSearchStudentsWithFiltersAndCaseInsensitive() {
        Student student = createStudentWithDetails("00000019L", "Student", "Surname", "Maths",
                CurrentYear.FIRST, false);
        Student student1 = createStudentWithDetails("00000020C", "Student1", "Surname1", "Teacher",
                CurrentYear.SECOND, true);
        Student student2 = createStudentWithDetails("00000021K", "Student2", "Surname2", "Developer",
                CurrentYear.FIFTH, false);

        Student saved = studentRepository.save(student);
        Student saved1 = studentRepository.save(student1);
        Student saved2 = studentRepository.save(student2);

        StudentSearchCriteria criteria = new StudentSearchCriteria("student", null, null, null);
        Pagination pagination = new Pagination(0, 20);
        PageResult<Student> result = studentRepository.search(criteria, pagination);

        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getPage()).isZero();
        assertThat(result.getSize()).isEqualTo(20);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getContent())
                .extracting(s -> s.getDni().getValue())
                .containsExactlyInAnyOrder("00000019L", "00000020C", "00000021K");
    }

    @Test
    @DisplayName("Should apply page and size filters")
    void shouldSearchStudentsAndApplyPageAndSizeFilters() {
        Student student = createStudentWithDetails("00000022E", "Student", "Surname", "Maths",
                CurrentYear.FIRST, false);
        Student student1 = createStudentWithDetails("00000023T", "Student1", "Surname1", "Teacher",
                CurrentYear.SECOND, true);
        Student student2 = createStudentWithDetails("00000024R", "Student2", "Surname2", "Developer",
                CurrentYear.FIFTH, false);

        Student saved = studentRepository.save(student);
        Student saved1 = studentRepository.save(student1);
        Student saved2 = studentRepository.save(student2);

        StudentSearchCriteria criteria = new StudentSearchCriteria(null, null, null, null);

        Pagination page1 = new Pagination(0, 2);
        Pagination page2 = new Pagination(1, 2);
        PageResult<Student> result1 = studentRepository.search(criteria, page1);
        PageResult<Student> result2 = studentRepository.search(criteria, page2);

        assertThat(result1.getContent()).hasSize(2);
        assertThat(result1.getTotalPages()).isEqualTo(2);
        assertThat(result1.getTotalElements()).isEqualTo(3);

        assertThat(result2.getContent()).hasSize(1);
        assertThat(result2.getTotalPages()).isEqualTo(2);
    }
}