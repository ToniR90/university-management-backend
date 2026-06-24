package com.orientation.backend.users.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientation.backend.shared.application.exceptions.core.BusinessViolation;
import com.orientation.backend.shared.application.exceptions.core.ErrorCode;
import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.users.application.exceptions.advisors.CreatedAdvisorException;
import com.orientation.backend.users.application.services.AdvisorService;
import com.orientation.backend.users.domain.model.entities.Advisor;
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
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdvisorController.class)
class AdvisorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdvisorService advisorService;

    @Autowired
    private ObjectMapper objectMapper;

    // ========== Helper Methods ==========

    private Advisor createTestAdvisor() {
        return Advisor.builder()
                .id(UUID.randomUUID())
                .dni(Dni.of("12345678Z"))
                .fullName(FullName.of("test_advisor_name", "test_advisor_surname"))
                .email(Optional.of(Email.of("advisor@email.com")))
                .phone(Optional.of(Phone.of("+34612345678")))
                .build();
    }

    // ========== POST / (Create Advisor) ==========

    @Test
    void shouldCreateAdvisor() throws Exception {
        // ARRANGE
        Advisor advisor = createTestAdvisor();
        when(advisorService.createAdvisor(any())).thenReturn(advisor);

        Map<String, Object> request = Map.of(
                "dni", "12345678Z",
                "name", "test_advisor_name",
                "surname", "test_advisor_surname",
                "email", "advisor@email.com",
                "phone", "+34612345678"
        );

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/advisors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dni").value("12345678Z"))
                .andExpect(jsonPath("$.name").value("test_advisor_name"));
    }

    @Test
    void shouldReturn400WhenDniIsBlank() throws Exception {
        // ARRANGE
        Map<String, Object> request = Map.of(
                "dni", "",
                "name", "test_advisor_name",
                "surname", "test_advisor_surname",
                "email", "advisor@email.com",
                "phone", "+34612345678"
        );

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/advisors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        // ARRANGE
        Map<String, Object> request = Map.of(
                "dni", "12345678Z",
                "name", "",
                "surname", "test_advisor_surname",
                "email", "advisor@email.com",
                "phone", "+34612345678"
        );

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/advisors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenEmailFormatIsInvalid() throws Exception {
        // ARRANGE
        Map<String, Object> request = Map.of(
                "dni", "12345678Z",
                "name", "test_advisor_name",
                "surname", "test_advisor_surname",
                "email", "invalid email",
                "phone", "+34612345678"
        );

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/advisors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenDniAlreadyExists() throws Exception {
        // ARRANGE
        BusinessViolation violation = new BusinessViolation("dni", "DNI already exists",
                ErrorCode.USER_ALREADY_EXISTS);
        when(advisorService.createAdvisor(any())).thenThrow(new CreatedAdvisorException(List.of(violation)));

        Map<String, Object> request = Map.of(
                "dni", "12345678Z",
                "name", "test_advisor_test",
                "surname", "test_advisor_surname",
                "email", "advisor@email.com",
                "phone", "+3461234578"
        );

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/advisors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.errorCode").value("USER_ALREADY_EXISTS"))
                .andExpect(jsonPath("$.message").value("Multiple business rule violations"))
                .andExpect(jsonPath("$.errors[0].field").value("dni"))
                .andExpect(jsonPath("$.errors[0].message").value("DNI already exists"));
    }

    // ========== GET / (Pagination & Filtering) ==========

    @Test
    void shouldGetAllAdvisorsWithDefaultPagination() throws Exception {
        // ARRANGE
        Advisor advisor = createTestAdvisor();
        PageResult<Advisor> pageResult = new PageResult<>(
                List.of(advisor), 0, 20, 1, 1
        );

        when(advisorService.searchAdvisors(any(), any())).thenReturn(pageResult);

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/advisors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].dni").value("12345678Z"))
                .andExpect(jsonPath("$.content[0].name").value("test_advisor_name"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void shouldSearchAdvisorsWithNameFilter() throws Exception {
        // ARRANGE
        Advisor advisor = createTestAdvisor();
        PageResult<Advisor> pageResult = new PageResult<>(
                List.of(advisor), 0, 20, 1, 1
        );

        when(advisorService.searchAdvisors(any(), any())).thenReturn(pageResult);

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/advisors")
                .param("name", "test_advisor_name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].dni").value("12345678Z"));
    }

    @Test
    void shouldReturn400WhenPageIsNegative() throws Exception {
        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/advisors")
                .param("page", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenSizeIsZero() throws Exception {
        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/advisors")
                .param("size", "0"))
                .andExpect(status().isBadRequest());
    }
}