package com.orientation.backend.users.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientation.backend.users.application.exceptions.DuplicateDniException;
import com.orientation.backend.users.application.exceptions.InvalidStudentOperationException;
import com.orientation.backend.users.application.exceptions.StudentNotFoundException;
import com.orientation.backend.users.application.services.StudentService;
import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.valueobjects.*;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .degree("Informàtica")
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
                .andExpect(jsonPath("$.error").value("Not Found"));
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
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    // ========== GET / ==========
    @Test
    void shouldGetAllStudents() throws Exception {
        Student student = createTestStudent();
        when(studentService.findAll()).thenReturn(List.of(student));

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].dni").value("12345678Z"))
                .andExpect(jsonPath("$[0].name").value("Joan"));
    }

    @Test
    void shouldReturnEmptyList() throws Exception {
        when(studentService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
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
        when(studentService.createStudent(any())).thenThrow(new DuplicateDniException("12345678Z"));

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
                .andExpect(jsonPath("$.status").value(409));
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
        when(studentService.updateContactInfo(eq(1L), any()))
                .thenThrow(new InvalidStudentOperationException("No es pot actualitzar sense cap dada de contacte"));

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
        when(studentService.updateRgpdConsent(eq(1L), any()))
                .thenThrow(new InvalidStudentOperationException("Status RGPD no vàlid: INVALID"));

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
        when(studentService.markAsAlumni(eq(1L), any()))
                .thenThrow(new InvalidStudentOperationException("Tipus d'alumni no vàlid: INVALID"));

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
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}