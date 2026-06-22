package com.orientation.backend.sessions.infraestructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orientation.backend.sessions.application.commands.CancelSessionCommand;
import com.orientation.backend.sessions.application.services.SessionService;
import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.domain.model.enums.SessionOrigin;
import com.orientation.backend.sessions.domain.model.enums.SessionType;
import com.orientation.backend.sessions.domain.model.exceptions.AssistantNotFoundException;
import com.orientation.backend.sessions.domain.model.exceptions.InvalidCancellationReasonException;
import com.orientation.backend.sessions.domain.model.exceptions.PersonNotFoundException;
import com.orientation.backend.sessions.domain.model.exceptions.SessionAlreadyInactiveException;
import com.orientation.backend.sessions.domain.model.exceptions.SessionNotFoundException;
import com.orientation.backend.shared.domain.model.query.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @Autowired
    private ObjectMapper objectMapper;

    // ========== Helper Methods ==========

    private Session createTestSession() {
        return Session.builder()
                .title("Test session")
                .description("Description for a test session")
                .motivation("Testing")
                .sessionType(SessionType.IN_PERSON)
                .sessionOrigin(SessionOrigin.OFFERED)
                .allWelcome(true)
                .startDateTime(LocalDateTime.now())
                .endDateTime(null)
                .cancelledAt(null)
                .cancelledReason(null)
                .infoSentAt(null)
                .score(9)
                .summary("Session summary")
                .build();
    }

    private Session createCancellableTestSession(UUID id) {
        return Session.builder()
                .id(id)
                .title("Cancellable test session")
                .description("Description for a cancellable test session")
                .motivation("Testing")
                .sessionType(SessionType.IN_PERSON)
                .sessionOrigin(SessionOrigin.OFFERED)
                .allWelcome(true)
                .startDateTime(LocalDateTime.now().plusDays(1))
                .endDateTime(null)
                .cancelledAt(null)
                .cancelledReason(null)
                .infoSentAt(null)
                .score(9)
                .summary("Session summary")
                .build();
    }

    // ========== POST / (Create Session) ==========

    @Test
    void shouldCreateSession() throws Exception {
        // ARRANGE
        Session session = createTestSession();
        when(sessionService.createSession(any())).thenReturn(session);

        // ACT + ASSERT
        Map<String, Object> request = Map.of(
                "title", "Test session",
                "description", "Test description",
                "motivation", "Testing",
                "sessionType", "IN_PERSON",
                "sessionOrigin", "OFFERED",
                "allWelcome", true
        );

        mockMvc.perform(post("/api/v1/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test session"))
                .andExpect(jsonPath("$.description").value("Description for a test session"));
    }

    @Test
    void shouldReturn400WhenTitleIsBlank() throws Exception {
        // ARRANGE
        Map<String, Object> request = Map.of(
                "title", "",
                "sessionType", "IN_PERSON",
                "sessionOrigin", "OFFERED"
        );
        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void shouldReturn400WhenSessionTypeIsInvalid() throws Exception {
        // ARRANGE
        when(sessionService.createSession(any())).thenThrow(new IllegalArgumentException("Invalid session type"));

        Map<String, Object> request = Map.of(
                "title", "Test session",
                "sessionType", "INVALID_TYPE",
                "sessionOrigin", "OFFERED"
        );

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ========== GET / (Pagination & Filtering) ==========

    @Test
    void shouldGetAllSessionsWithDefaultPagination() throws Exception {
        // ARRANGE
        Session session = createTestSession();
        PageResult<Session> pageResult = new PageResult<>(
                List.of(session), 0, 20, 1, 1
        );
        when(sessionService.searchSessions(any(), any())).thenReturn(pageResult);

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test session"))
                .andExpect(jsonPath("$.content[0].description").value("Description for a test session"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void shouldSearchSessionsWithTitleFilter() throws Exception {
        // ARRANGE
        Session session = createTestSession();
        PageResult<Session> pageResult = new PageResult<>(List.of(session), 0, 20, 1, 1);

        when(sessionService.searchSessions(any(), any())).thenReturn(pageResult);

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/sessions")
                        .param("title", "Test session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test session"));

        // VERIFY
        verify(sessionService).searchSessions(any(), any());
    }

    @Test
    void shouldReturn400WhenPageIsNegative() throws Exception {
        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/sessions")
                .param("page", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenSizeIsZero() throws Exception {
        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/sessions")
                .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    // ========== GET /cancellable ==========

    @Test
    void shouldGetCancellableSessions() throws Exception {
        // ARRANGE
        Session session = createCancellableTestSession(UUID.randomUUID());
        when(sessionService.findCancellable(any())).thenReturn(List.of(session));

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/sessions/cancellable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Cancellable test session"));
    }

    @Test
    void shouldGetCancellableSessionsFilteredByTitle() throws Exception {
        // ARRANGE
        Session session = createCancellableTestSession(UUID.randomUUID());
        when(sessionService.findCancellable(any())).thenReturn(List.of(session));

        // ACT + ASSERT
        mockMvc.perform(get("/api/v1/sessions/cancellable")
                        .param("title", "Cancellable test session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Cancellable test session"));
    }

    // ========== PATCH /{id}/cancel ==========

    @Test
    void shouldCancelSession() throws Exception {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        doNothing().when(sessionService).cancelSession(any());

        Map<String, Object> request = Map.of("cancelReason", "Test cancel reason");

        // ACT + ASSERT
        mockMvc.perform(patch("/api/v1/sessions/" + sessionId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(sessionService, times(1)).cancelSession(any());
    }

    @Test
    void shouldReturn404WhenCancellingNonExistentSession() throws Exception {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        doThrow(new SessionNotFoundException()).when(sessionService).cancelSession(any());

        Map<String, Object> request = Map.of("cancelReason", "Test cancel reason");

        // ACT + ASSERT
        mockMvc.perform(patch("/api/v1/sessions/" + sessionId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturn409WhenCancellingAlreadyCancelledSession() throws Exception {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        doThrow(new SessionAlreadyInactiveException()).when(sessionService).cancelSession(any());

        Map<String, Object> request = Map.of("cancelReason", "Test cancel reason");

        // ACT + ASSERT
        mockMvc.perform(patch("/api/v1/sessions/" + sessionId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void shouldReturn400WhenCancelReasonIsBlank() throws Exception {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        doThrow(new InvalidCancellationReasonException()).when(sessionService).cancelSession(any());

        Map<String, Object> request = Map.of("cancelReason", "Test cancel reason");

        // ACT + ASSERT
        mockMvc.perform(patch("/api/v1/sessions/" + sessionId + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    // ========== POST /{sessionId}/assistants ==========

    @Test
    void shouldAddAssistant() throws Exception {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        doNothing().when(sessionService).addAssistant(any());

        Map<String, Object> request = Map.of("personDni", "00000001R");

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/sessions/" + sessionId + "/assistants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(sessionService, times(1)).addAssistant(any());
    }

    @Test
    void shouldReturn404WhenAddingAssistantToNonExistentSession() throws Exception {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        doThrow(new SessionNotFoundException()).when(sessionService).addAssistant(any());

        Map<String, Object> request = Map.of("personDni", "00000001R");

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/sessions/" + sessionId + "/assistants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturn404WhenAddingAssistantWithUnknownDni() throws Exception {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        doThrow(new PersonNotFoundException()).when(sessionService).addAssistant(any());

        Map<String, Object> request = Map.of("personDni", "00000001R");

        // ACT + ASSERT
        mockMvc.perform(post("/api/v1/sessions/" + sessionId + "/assistants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // ========== DELETE /{sessionId}/assistants/{dni} ==========

    @Test
    void shouldRemoveAssistant() throws Exception {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        String dni = "00000001R";
        doNothing().when(sessionService).removeAssistant(any());

        // ACT + ASSERT
        mockMvc.perform(delete("/api/v1/sessions/" + sessionId + "/assistants/" + dni))
                .andExpect(status().isNoContent());

        verify(sessionService, times(1)).removeAssistant(any());
    }

    @Test
    void shouldReturn404WhenRemovingAssistantNotInSession() throws Exception {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        String dni = "00000001R";
        doThrow(new AssistantNotFoundException()).when(sessionService).removeAssistant(any());

        Map<String, Object> request = Map.of("personDni", "00000001R");

        // ACT + ASSERT
        mockMvc.perform(delete("/api/v1/sessions/" + sessionId + "/assistants/" + dni)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}