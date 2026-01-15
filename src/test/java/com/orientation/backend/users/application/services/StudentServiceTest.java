package com.orientation.backend.users.application.services;

import com.orientation.backend.users.application.commands.CreateStudentCommand;
import com.orientation.backend.users.application.exceptions.DuplicateDniException;
import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.valueobjects.*;
import com.orientation.backend.users.domain.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    // ========== Helper Method ==========
    private Student createTestStudent(String dni, String email) {
        return Student.builder()
                .dni(Dni.of(dni))
                .fullName(FullName.of("test_name", "test_firstSurname", "test_secondSurname"))
                .email(Optional.of(Email.of(email)))
                .phone(Optional.of(Phone.of("+34612345678")))
                .degree("Videojocs")
                .currentYear(CurrentYear.FIRST)
                .alumniInfo(AlumniInfo.notAlumni())
                .rgpdConsent(RgpdConsent.pending())
                .build();
    }

    private CreateStudentCommand createTestStudentCommand(String dni, String email) {

        return new CreateStudentCommand(
                dni,
                "test_name",
                "test_firstSurname",
                "test_secondSurname",
                email,
                "+34612345678",
                "Videojocs",
                "FIRST"
        );
    }

    @Test
    void shouldCreateStudentSuccessfully() {

        // ARRANGE
        CreateStudentCommand command = createTestStudentCommand("00000000T", "test@email.com");

        Student expectedStudent = createTestStudent("00000000T", "test@email.com");

        // Forced false with any Dni match
        when(studentRepository.existsByDni(any(Dni.class))).thenReturn(false);

        // Forced to return the expectedStudent
        when(studentRepository.save(any(Student.class))).thenReturn(expectedStudent);

        // ACT
        Student result = studentService.createStudent(command);

        // ASSERT
        assertNotNull(result);
        assertEquals("00000000T", result.getDni().getValue());
        assertEquals("test_name", result.getFullName().getName());
        assertEquals("test_firstSurname", result.getFullName().getFirstSurname());
        assertTrue(result.getFullName().getSecondSurname().isPresent());
        assertEquals("test_secondSurname", result.getFullName().getSecondSurname().get());

        assertTrue(result.getEmail().isPresent());
        assertEquals("test@email.com", result.getEmail().get().getValue());

        assertTrue(result.getPhone().isPresent());
        assertEquals("+34612345678", result.getPhone().get().getValue());

        assertEquals(CurrentYear.FIRST, result.getCurrentYear());

        // VERIFY
        verify(studentRepository, times(1))
                .existsByDni(any(Dni.class));

        verify(studentRepository, times(1))
                .save(any(Student.class));
    }

    @Test
    void shouldThrowExceptionWhenDniAlreadyExists() {

        // ARRANGE
        CreateStudentCommand command = createTestStudentCommand("00000001R", "test@email.com");

        when(studentRepository.existsByDni(any(Dni.class))).thenReturn(true);

        // ACT + ASSERT
        assertThrows(DuplicateDniException.class, () -> studentService.createStudent(command));

        //VERIFY
        verify(studentRepository, times(1))
                .existsByDni(any(Dni.class));

        verify(studentRepository, never())
                .save(any(Student.class));
    }

}