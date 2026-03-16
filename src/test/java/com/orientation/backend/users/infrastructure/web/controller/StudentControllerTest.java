package com.orientation.backend.users.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientation.backend.users.application.exceptions.CreatedStudentException;
import com.orientation.backend.users.application.exceptions.UpdateStudentException;
import com.orientation.backend.users.application.exceptions.StudentNotFoundException;
import com.orientation.backend.users.application.exceptions.core.BusinessViolation;
import com.orientation.backend.users.application.exceptions.core.ErrorCode;
import com.orientation.backend.users.application.services.StudentService;
import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.Degree;
import com.orientation.backend.users.domain.model.query.PageResult;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.model.valueobjects.Email;
import com.orientation.backend.users.domain.model.valueobjects.FullName;
import com.orientation.backend.users.domain.model.valueobjects.Phone;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StudentService studentService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	// ========== Helper ==========
	private Student createTestStudent() {
		return Student.builder()
				.id(1L)
				.dni(Dni.of("12345678Z"))
				.fullName(FullName.of("Joan", "García", "López"))
				.email(Email.of("joan@mail.com"))
				.phone(Phone.of("600123456"))
				.degree(Degree.DIGITAL_MARKETING)
				.currentYear(CurrentYear.FIRST)
				.build();
	}
	
	// ========== GET /{id} ==========
	@Test
	void shouldGetStudentById() throws Exception {
		Student student = createTestStudent();
		when(studentService.findById(1L)).thenReturn(student);
		
		mockMvc.perform(get("/api/v1/students/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.dni").value("12345678Z"))
				.andExpect(jsonPath("$.name").value("Joan"));
	}
	
	@Test
	void shouldReturn404WhenStudentNotFoundById() throws Exception {
		when(studentService.findById(999L)).thenThrow(new StudentNotFoundException(999L));
		
		mockMvc.perform(get("/api/v1/students/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"))
				.andExpect(jsonPath("$.message").value("Multiple business rule violations"))
				.andExpect(jsonPath("$.errors[0].field").value("id"))
				.andExpect(jsonPath("$.errors[0].message").value("User not found with id 999"));
	}
	
	// ========== GET /dni/{dni} ==========
	@Test
	void shouldGetStudentByDni() throws Exception {
		Student student = createTestStudent();
		when(studentService.findByDni("12345678Z")).thenReturn(student);
		
		mockMvc.perform(get("/api/v1/students/dni/12345678Z"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.dni").value("12345678Z"))
				.andExpect(jsonPath("$.name").value("Joan"));
	}
	
	@Test
	void shouldReturn404WhenStudentNotFoundByDni() throws Exception {
		when(studentService.findByDni("21273746B")).thenThrow(new StudentNotFoundException("21273746B"));
		
		mockMvc.perform(get("/api/v1/students/dni/21273746B"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"))
				.andExpect(jsonPath("$.message").value("Multiple business rule violations"))
				.andExpect(jsonPath("$.errors[0].field").value("dni"))
				.andExpect(jsonPath("$.errors[0].message").value("User not found with dni 21273746B"));
	}

	// ========== GET / (Pagination & Filtering) ==========

	@Test
	void shouldGetAllStudentsWithDefaultPagination() throws Exception {
		// ARRANGE
		Student student = createTestStudent();
		PageResult<Student> pageResult = new PageResult<>(
				List.of(student), 0, 20, 1, 1
		);

		when(studentService.searchStudents(any(), any())).thenReturn(pageResult);

		// ACT + ASSERT
		mockMvc.perform(get("/api/v1/students"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content.length()").value(1))
				.andExpect(jsonPath("$.content[0].dni").value("12345678Z"))
				.andExpect(jsonPath("$.content[0].name").value("Joan"))
				.andExpect(jsonPath("$.page").value(0))
				.andExpect(jsonPath("$.size").value(20))
				.andExpect(jsonPath("$.totalElements").value(1))
				.andExpect(jsonPath("$.totalPages").value(1));
	}

	@Test
	void shouldReturnEmptyPagedResult() throws Exception {
		// ARRANGE
		PageResult<Student> emptyResult = new PageResult<>(
				List.of(), 0, 20, 0, 0
		);

		when(studentService.searchStudents(any(), any())).thenReturn(emptyResult);

		// ACT + ASSERT
		mockMvc.perform(get("/api/v1/students"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content.length()").value(0))
				.andExpect(jsonPath("$.totalElements").value(0))
				.andExpect(jsonPath("$.totalPages").value(0));
	}

	@Test
	void shouldSearchStudentsWithFilters() throws Exception {
		// ARRANGE
		Student student = createTestStudent();
		PageResult<Student> pageResult = new PageResult<>(
				List.of(student), 0, 20, 1, 1
		);

		when(studentService.searchStudents(any(), any())).thenReturn(pageResult);

		// ACT + ASSERT
		mockMvc.perform(get("/api/v1/students")
						.param("name", "Joan")
						.param("currentYear", "FIRST")
						.param("isAlumni", "false"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()").value(1))
				.andExpect(jsonPath("$.content[0].dni").value("12345678Z"));

		// VERIFY
		verify(studentService).searchStudents(any(), any());
	}

	@Test
	void shouldSearchStudentsWithCustomPagination() throws Exception {
		// ARRANGE
		Student student = createTestStudent();
		PageResult<Student> pageResult = new PageResult<>(
				List.of(student), 2, 5, 11, 3
		);

		when(studentService.searchStudents(any(), any())).thenReturn(pageResult);

		// ACT + ASSERT
		mockMvc.perform(get("/api/v1/students")
						.param("page", "2")
						.param("size", "5"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.page").value(2))
				.andExpect(jsonPath("$.size").value(5))
				.andExpect(jsonPath("$.totalElements").value(11))
				.andExpect(jsonPath("$.totalPages").value(3));
	}

	@Test
	void shouldReturn400WhenPageIsNegative() throws Exception {
		// ACT + ASSERT
		mockMvc.perform(get("/api/v1/students")
						.param("page", "-1"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturn400WhenSizeIsZero() throws Exception {
		mockMvc.perform(get("/api/v1/students")
						.param("size", "0"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturn400WhenSizeExceedsMax() throws Exception {
		mockMvc.perform(get("/api/v1/students")
						.param("size", "200"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void shouldReturn400WhenCurrentYearIsInvalid() throws Exception {
		mockMvc.perform(get("/api/v1/students")
						.param("currentYear", "INVALID"))
				.andExpect(status().isBadRequest());
	}


	// ========== POST / ==========
	@Test
	void shouldCreateStudent() throws Exception {
		Student student = createTestStudent();
		when(studentService.createStudent(any())).thenReturn(student);
		
		Map<String, Object> request = Map.of(
				"dni", "12345678Z",
				"name", "Joan",
				"firstSurname", "García",
				"degree", "Informàtica",
				"currentYear", "FIRST"
		);
		
		mockMvc.perform(post("/api/v1/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.dni").value("12345678Z"))
				.andExpect(jsonPath("$.name").value("Joan"));
	}
	
	@Test
	void shouldReturn409WhenDuplicateDni() throws Exception {
		BusinessViolation violation = new BusinessViolation("dni", "DNI already exists",
				ErrorCode.USER_ALREADY_EXISTS);
		when(studentService.createStudent(any())).thenThrow(new CreatedStudentException(List.of(violation)));
		
		Map<String, Object> request = Map.of(
				"dni", "12345678Z",
				"name", "Joan",
				"firstSurname", "García",
				"degree", "Informàtica",
				"currentYear", "FIRST"
		);
		
		mockMvc.perform(post("/api/v1/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.errorCode").value("USER_ALREADY_EXISTS"))
				.andExpect(jsonPath("$.message").value("Multiple business rule violations"))
				.andExpect(jsonPath("$.errors[0].field").value("dni"))
				.andExpect(jsonPath("$.errors[0].message").value("DNI already exists"));
	}
	
	@Test
	void shouldReturn400WhenMissingRequiredFields() throws Exception {
		Map<String, Object> request = Map.of();
		
		mockMvc.perform(post("/api/v1/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}
	
	// ========== PATCH /{id}/contact ==========
	@Test
	void shouldUpdateContact() throws Exception {
		Student student = createTestStudent();
		when(studentService.updateContactInfo(eq(1L), any())).thenReturn(student);
		
		Map<String, Object> request = Map.of(
				"email", "nou@mail.com",
				"phone", "600654321"
		);
		
		mockMvc.perform(patch("/api/v1/students/1/contact")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dni").value("12345678Z"));
	}
	
	@Test
	void shouldReturn400WhenBothContactFieldsNull() throws Exception {
		BusinessViolation violation = new BusinessViolation("contact", "No es pot actualitzar sense cap dada de " +
				"contacte",
				ErrorCode.BUSINESS_RULE_VIOLATION);
		when(studentService.updateContactInfo(eq(1L), any()))
				.thenThrow(new UpdateStudentException(List.of(violation)));
		
		String request = "{\"email\": null, \"phone\": null}";
		
		mockMvc.perform(patch("/api/v1/students/1/contact")
						.contentType(MediaType.APPLICATION_JSON)
						.content(request))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}
	
	// ========== PATCH /{id}/rgpd ==========
	@Test
	void shouldUpdateRgpd() throws Exception {
		Student student = createTestStudent();
		when(studentService.updateRgpdConsent(eq(1L), any())).thenReturn(student);
		
		Map<String, Object> request = Map.of(
				"rgpdConsentStatus", "SIGNED_IN_PERSON"
		);
		
		mockMvc.perform(patch("/api/v1/students/1/rgpd")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dni").value("12345678Z"));
	}
	
	@Test
	void shouldReturn400WhenInvalidRgpdStatus() throws Exception {
		BusinessViolation violation = new BusinessViolation(
				"rgpdConsentStatus",
				"Status RGPD no vàlid: " + 1L,
				ErrorCode.BUSINESS_RULE_VIOLATION
		);
		when(studentService.updateRgpdConsent(eq(1L), any()))
				.thenThrow(new UpdateStudentException(List.of(violation)));
		
		Map<String, Object> request = Map.of(
				"rgpdConsentStatus", "INVALID"
		);
		
		mockMvc.perform(patch("/api/v1/students/1/rgpd")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}
	
	// ========== PATCH /{id}/alumni ==========
	@Test
	void shouldMarkAsAlumni() throws Exception {
		Student student = createTestStudent();
		when(studentService.markAsAlumni(eq(1L), any())).thenReturn(student);
		
		Map<String, Object> request = Map.of(
				"alumniType", "BACHELOR",
				"graduationYear", 2023
		);
		
		mockMvc.perform(patch("/api/v1/students/1/alumni")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dni").value("12345678Z"));
	}
	
	@Test
	void shouldReturn400WhenInvalidAlumniType() throws Exception {
		BusinessViolation violation = new BusinessViolation(
				"alumniType",
				"Tipus d'alumni no vàlid: " + 1L,
				ErrorCode.BUSINESS_RULE_VIOLATION
		);
		when(studentService.markAsAlumni(eq(1L), any()))
				.thenThrow(new UpdateStudentException(List.of(violation)));
		
		Map<String, Object> request = Map.of(
				"alumniType", "INVALID",
				"graduationYear", 2023
		);
		
		mockMvc.perform(patch("/api/v1/students/1/alumni")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}
	
	// ========== DELETE /{id} ==========
	@Test
	void shouldDeleteStudent() throws Exception {
		doNothing().when(studentService).deleteStudent(1L);
		
		mockMvc.perform(delete("/api/v1/students/1"))
				.andExpect(status().isNoContent());
		
		verify(studentService, times(1)).deleteStudent(1L);
	}
	
	@Test
	void shouldReturn404WhenDeletingNonExistentStudent() throws Exception {
		doThrow(new StudentNotFoundException(999L)).when(studentService).deleteStudent(999L);
		
		mockMvc.perform(delete("/api/v1/students/999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.errorCode").value("USER_NOT_FOUND"))
				.andExpect(jsonPath("$.message").value("Multiple business rule violations"))
				.andExpect(jsonPath("$.errors[0].field").value("id"))
				.andExpect(jsonPath("$.errors[0].message").value("User not found with id 999"));
		
	}


}