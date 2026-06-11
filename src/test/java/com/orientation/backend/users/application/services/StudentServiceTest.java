package com.orientation.backend.users.application.services;

import com.orientation.backend.users.application.commands.CreateStudentCommand;
import com.orientation.backend.users.application.commands.MarkAsAlumniCommand;
import com.orientation.backend.users.application.commands.UpdateContactCommand;
import com.orientation.backend.users.application.commands.UpdateRgpdConsentCommand;
import com.orientation.backend.users.application.exceptions.CreatedStudentException;
import com.orientation.backend.users.application.exceptions.StudentNotFoundException;
import com.orientation.backend.users.application.exceptions.UpdateStudentException;
import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.Degree;
import com.orientation.backend.users.domain.model.enums.RgpdConsentStatus;
import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.domain.model.query.StudentSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.*;
import com.orientation.backend.users.domain.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
	
	@Mock
	private StudentRepository studentRepository;
	
	@InjectMocks
	private StudentService studentService;
	
	// ========== Helper Methods ==========
	private Student createTestStudent(String dni, String email) {
		return Student.builder()
				.id(UUID.randomUUID())
				.dni(Dni.of(dni))
				.fullName(FullName.of("test_name", "test_surname"))
				.email(Optional.of(Email.of(email)))
				.phone(Optional.of(Phone.of("+34612345678")))
				.degree(Degree.VIDEOGAME_DESIGN)
				.currentYear(CurrentYear.FIRST)
				.alumniInfo(AlumniInfo.notAlumni())
				.rgpdConsent(RgpdConsent.pending())
				.build();
	}
	
	private Student createTestStudentWithoutOptionals(String dni) {
		return Student.builder()
				.dni(Dni.of(dni))
				.fullName(FullName.of("test_name", "test_firstSurname"))
				.email(Optional.empty())
				.phone(Optional.empty())
				.degree(Degree.VIDEOGAME_DESIGN)
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
				"test@email.com",
				"+34612345678",
				"VIDEOGAME_DESIGN",
				"FIRST"
		);
	}
	
	@Test
	void shouldCreateStudentSuccessfully() {
		
		// ARRANGE
		CreateStudentCommand command = createTestStudentCommand("00000000T", "test@email.com");
		
		Student expectedStudent = createTestStudent("00000000T", "test@email.com");
		
		when(studentRepository.existsByDni(any(Dni.class))).thenReturn(false);
		when(studentRepository.save(any(Student.class))).thenReturn(expectedStudent);
		
		// ACT
		Student result = studentService.createStudent(command);
		
		// ASSERT
		assertNotNull(result);
		assertEquals("00000000T", result.getDni().getValue());
		assertEquals("test_name", result.getFullName().getName());
		assertEquals("test_surname", result.getFullName().getSurname());
		
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
	
	// =====================================================
	// CREATE STUDENT TESTS
	// =====================================================
	
	@Test
	void shouldThrowExceptionWhenDniAlreadyExists() {
		
		// ARRANGE
		CreateStudentCommand command = createTestStudentCommand("00000001R", "test@email.com");
		
		when(studentRepository.existsByDni(any(Dni.class))).thenReturn(true);
		
		// ACT + ASSERT
		assertThrows(CreatedStudentException.class, () -> studentService.createStudent(command));
		
		//VERIFY
		verify(studentRepository, times(1))
				.existsByDni(any(Dni.class));
		
		verify(studentRepository, never())
				.save(any(Student.class));
	}
	
	@Test
	void shouldCreateStudentWithoutOptionalFields() {
		
		// ARRANGE
		CreateStudentCommand command = new CreateStudentCommand(
				"00000002W",
				"test_name",
				"test_surname",
				null,
				null,
				"VIDEOGAME_DESIGN",
				"FIRST"
		);
		
		Student expectedStudent = createTestStudentWithoutOptionals("00000002W");
		
		when(studentRepository.existsByDni(any(Dni.class))).thenReturn(false);
		when(studentRepository.save(any(Student.class))).thenReturn(expectedStudent);
		
		// ACT
		Student result = studentService.createStudent(command);
		
		// ASSERT
		assertTrue(result.getEmail().isEmpty());
		assertTrue(result.getPhone().isEmpty());
		
		// VERIFY
		verify(studentRepository, times(1))
				.existsByDni(any(Dni.class));
		verify(studentRepository, times(1))
				.save(any(Student.class));
	}
	
	@Test
	void shouldValidateDniFormatOnCreate() {
		
		// ARRANGE
		CreateStudentCommand command = createTestStudentCommand("12345678A", "test@email.com");
		
		// ACT + ASSERT
		assertThrows(IllegalArgumentException.class, () -> studentService.createStudent(command));
		
		// VERIFY
		verify(studentRepository, never()).existsByDni(any(Dni.class));
		verify(studentRepository, never()).save(any(Student.class));
	}

	
	// ===================================================
	// FIND BY DNI TESTS
	// ===================================================
	
	@Test
	void shouldFindStudentByDni() {
		
		// ARRANGE
		Student student = createTestStudent("00000004G", "test@email.com");
		
		when(studentRepository.findByDni(Dni.of("00000004G"))).thenReturn(Optional.of(student));
		
		// ACT
		Student result = studentService.findByDni("00000004G");
		
		// ASSERT
		assertNotNull(student);
		assertEquals(student, result);
		assertEquals("00000004G", result.getDni().getValue());
		
		// VERIFY
		verify(studentRepository, times(1)).findByDni(Dni.of("00000004G"));
	}
	
	@Test
	void shouldThrowExceptionWhenStudentNotFoundByDni() {
		
		// ARRANGE
		when(studentRepository.findByDni(Dni.of("00000005M"))).thenReturn(Optional.empty());
		
		// ACT + ASSERT
		assertThrows(StudentNotFoundException.class, () -> studentService.findByDni("00000005M"));
		
		// VERIFY
		verify(studentRepository, times(1))
				.findByDni(Dni.of("00000005M"));
	}
	
	// ========================================================
	// UPDATE CONTACT INFO TESTS
	// ========================================================
	
	@Test
	void shouldUpdateContactInfo() {
		
		// ARRANGE
		UpdateContactCommand command = new UpdateContactCommand("newmail@email.com", "+3461234587");
		Student student = createTestStudent("00000006Y", "test@email.com");
		
		when(studentRepository.findByDni(Dni.of(student.getDni().getValue()))).thenReturn(Optional.of(student));
		when(studentRepository.save(any(Student.class))).thenReturn(student);
		
		// ACT
		Student result = studentService.updateContactInfo(student.getDni().getValue(), command);
		
		// ASSERT
		assertNotNull(result);
		assertTrue(result.getEmail().isPresent());
		assertEquals("newmail@email.com", result.getEmail().get().getValue());
		assertTrue(result.getPhone().isPresent());
		assertEquals("+3461234587", result.getPhone().get().getValue());
		
		// VERIFY
		verify(studentRepository, times(1)).findByDni(Dni.of(student.getDni().getValue()));
		verify(studentRepository, times(1)).save(any(Student.class));
	}
	
	@Test
	void shouldUpdateOnlyEmail() {
		
		// ARRANGE
		Student student = createTestStudent("00000007F", "test@email.com");
		UpdateContactCommand command = new UpdateContactCommand("updated_mail@email.com", null);
		
		when(studentRepository.findByDni(Dni.of(student.getDni().getValue()))).thenReturn(Optional.of(student));
		when(studentRepository.save(any(Student.class))).thenReturn(student);
		
		// ACT
		Student result = studentService.updateContactInfo(student.getDni().getValue(), command);
		
		// ASSERT
		assertNotNull(result);
		assertTrue(result.getEmail().isPresent());
		assertEquals("updated_mail@email.com", result.getEmail().get().getValue());
		assertTrue(result.getPhone().isPresent());
		assertTrue(student.getPhone().isPresent());
		assertEquals(student.getPhone().get().getValue(), result.getPhone().get().getValue());
		
		// VERIFY
		verify(studentRepository, times(1)).findByDni(Dni.of(student.getDni().getValue()));
		verify(studentRepository, times(1)).save(any(Student.class));
	}
	
	@Test
	void shouldThrowExceptionWhenBothContactFieldsAreNull() {
		
		// ARRANGE
		Student student = createTestStudent("00000008P", "test@email.com");
		when(studentRepository.findByDni(student.getDni())).thenReturn(Optional.of(student));
		UpdateContactCommand command = new UpdateContactCommand(null, null);
		
		// ACT + ASSERT
		assertThrows(
				UpdateStudentException.class,
				() -> studentService.updateContactInfo(student.getDni().getValue(), command)
		);
	}
	
	// =================================================
	// UPDATE RGPD CONSENT TESTS
	// =================================================
	@Test
	void shouldUpdateRgpdConsentToSignedInPerson() {
		// ARRANGE
		Student student = createTestStudent("00000008P", "test@email.com");
		UpdateRgpdConsentCommand command = new UpdateRgpdConsentCommand("SIGNED_IN_PERSON", null);
		
		when(studentRepository.findByDni(student.getDni())).thenReturn(Optional.of(student));
		when(studentRepository.save(any(Student.class))).thenReturn(student);
		
		// ACT
		Student result = studentService.updateRgpdConsent(student.getDni().getValue(), command);
		
		// ASSERT
		assertNotNull(result);
		assertTrue(result.getRgpdConsent().getStatus().isSigned());
		assertEquals(RgpdConsentStatus.SIGNED_IN_PERSON, result.getRgpdConsent().getStatus());
		
		// VERIFY
		verify(studentRepository, times(1)).findByDni(student.getDni());
		verify(studentRepository, times(1)).save(any(Student.class));
	}
	
	@Test
	void shouldUpdateRgpdConsentToAlreadySigned() {
		// ARRANGE
		Student student = createTestStudent("00000009D", "test@email.com");
		UpdateRgpdConsentCommand command = new UpdateRgpdConsentCommand("ALREADY_SIGNED", 2020);
		
		when(studentRepository.findByDni(student.getDni())).thenReturn(Optional.of(student));
		when(studentRepository.save(any(Student.class))).thenReturn(student);
		
		// ACT
		Student result = studentService.updateRgpdConsent(student.getDni().getValue(), command);
		
		// ASSERT
		assertNotNull(result);
		assertTrue(result.getRgpdConsent().getStatus().isSigned());
		assertEquals(RgpdConsentStatus.ALREADY_SIGNED, result.getRgpdConsent().getStatus());
		
		// VERIFY
		verify(studentRepository, times(1)).findByDni(student.getDni());
		verify(studentRepository, times(1)).save(any(Student.class));
	}
	
	@Test
	void shouldThrowExceptionWhenInvalidRgpdStatus() {
		// ARRANGE
		Student student = createTestStudent("00000010X", "test@email.com");
		UpdateRgpdConsentCommand command = new UpdateRgpdConsentCommand("NOT_VALID_RGPD", null);
		
		when(studentRepository.findByDni(student.getDni())).thenReturn(Optional.of(student));
		
		// ACT + ASSERT
		assertThrows(UpdateStudentException.class, () -> studentService.updateRgpdConsent(student.getDni().getValue(), command));
		
		// VERIFY
		verify(studentRepository, times(1)).findByDni(student.getDni());
		verify(studentRepository, never()).save(any(Student.class));
	}
	
	// ===========================================
	// MARK AS ALUMNI TESTS
	// ===========================================
	@Test
	void shouldMarkAsAlumni() {
		// ARRANGE
		Student student = createTestStudent("00000011B", "test@email.com");
		MarkAsAlumniCommand command = new MarkAsAlumniCommand("BACHELOR", 2000);
		
		when(studentRepository.findByDni(student.getDni())).thenReturn(Optional.of(student));
		when(studentRepository.save(any(Student.class))).thenReturn(student);
		
		// ACT
		Student result = studentService.markAsAlumni(student.getDni().getValue(), command);
		
		// ASSERT
		assertNotNull(result);
		assertTrue(result.getAlumniInfo().isAlumni());
		
		// VERIFY
		verify(studentRepository, times(1)).findByDni(student.getDni());
		verify(studentRepository, times(1)).save(any(Student.class));
	}
	
	@Test
	void shouldThrowExceptionWhenInvalidAlumniType() {
		// ARRANGE
		Student student = createTestStudent("00000012N", "test@email.com");
		MarkAsAlumniCommand command = new MarkAsAlumniCommand("INVALID_ALUMNI_TYPE", 2000);
		
		when(studentRepository.findByDni(student.getDni())).thenReturn(Optional.of(student));
		
		// ACT + ASSERT
		assertThrows(UpdateStudentException.class, () -> studentService.markAsAlumni(student.getDni().getValue(), command));
		
		// VERIFY
		verify(studentRepository, times(1)).findByDni(student.getDni());
		verify(studentRepository, never()).save(any(Student.class));
		
	}
	
	@Test
	void shouldThrowExceptionWhenInvalidGraduationYear() {
		// ARRANGE
		Student student = createTestStudent("00000013J", "test@email.com");
		MarkAsAlumniCommand command = new MarkAsAlumniCommand("BACHELOR", 1800);
		
		when(studentRepository.findByDni(student.getDni())).thenReturn(Optional.of(student));
		
		// ACT + ASSERT
		assertThrows(UpdateStudentException.class, () -> studentService.markAsAlumni(student.getDni().getValue(), command));
		
		// VERIFY
		verify(studentRepository, times(1)).findByDni(student.getDni());
		verify(studentRepository, never()).save(any(Student.class));
	}
	
	// ===========================================
	// DELETE STUDENT TESTS
	// ===========================================
	
	@Test
	void shouldDeleteStudentSuccessfully() {
		// ARRANGE
		Student student = createTestStudent("00000014Z", "test@email.com");
		
		when(studentRepository.findByDni(student.getDni())).thenReturn(Optional.of(student));
		when(studentRepository.save(any(Student.class))).thenReturn(student);
		
		// ACT
		studentService.deleteStudent(student.getDni().getValue());
		
		// ASSERT
		assertFalse(student.isActive());
		assertTrue(student.getDeletedAt().isPresent());
		
		// VERIFY
		verify(studentRepository, times(1)).findByDni(student.getDni());
		verify(studentRepository, times(1)).save(any(Student.class));
	}
	
	@Test
	void shouldThrowExceptionWhenDeletingNonExistentStudent() {
		// ARRANGE
		when(studentRepository.findByDni(Dni.of("12345678Z"))).thenReturn(Optional.empty());
		
		// ACT + ASSERT
		assertThrows(StudentNotFoundException.class, () -> studentService.deleteStudent("12345678Z"));
		
		// VERIFY
		verify(studentRepository, times(1)).findByDni(Dni.of("12345678Z"));
		verify(studentRepository, never()).save(any(Student.class));
	}

	@Test
	void shouldSearchStudents() {
		// ARRANGE
		PageResult<Student> expectedResult = new PageResult<>(
				List.of(createTestStudent("00000015S", "test@email.com")),
				0, 20, 1, 1
		);
		StudentSearchCriteria criteria = new StudentSearchCriteria(null, null, null, null, null);
		Pagination pagination = new Pagination(0, 20);

		when(studentRepository.search(criteria, pagination)).thenReturn(expectedResult);

		// ACT
		PageResult<Student> result = studentService.searchStudents(criteria, pagination);

		// ASSERT
		assertNotNull(result);
		assertEquals(1, result.getContent().size());
		assertEquals(expectedResult, result);

		// VERIFY
		verify(studentRepository).search(criteria, pagination);
	}
}