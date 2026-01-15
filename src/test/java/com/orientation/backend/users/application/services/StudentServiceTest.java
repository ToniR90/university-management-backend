package com.orientation.backend.users.application.services;

import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.valueobjects.*;
import com.orientation.backend.users.domain.repository.StudentRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
                .fullName(FullName.of("Test_Name", "Test_Surname1", "Test_Surname2"))
                .email(Optional.of(Email.of(email)))
                .phone(Optional.of(Phone.of("+34612345678")))
                .degree("Videojocs")
                .currentYear(CurrentYear.FIRST)
                .alumniInfo(AlumniInfo.notAlumni())
                .rgpdConsent(RgpdConsent.pending())
                .build();
    }

}