package com.orientation.backend.sessions.application.services;

import com.orientation.backend.sessions.application.commands.*;
import com.orientation.backend.sessions.domain.model.entities.AdvisorInSession;
import com.orientation.backend.sessions.domain.model.entities.Assistant;
import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.domain.model.enums.SessionOrigin;
import com.orientation.backend.sessions.domain.model.enums.SessionType;
import com.orientation.backend.sessions.domain.model.exceptions.*;
import com.orientation.backend.sessions.domain.model.query.SessionSearchCriteria;
import com.orientation.backend.sessions.domain.repository.*;
import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private AdvisorInSessionRepository advisorInSessionRepository;

    @Mock
    private AdvisorLookupPort advisorLookupPort;

    @Mock
    private PersonLookupPort personLookupPort;

    @Mock
    private AssistantRepository assistantRepository;

    @InjectMocks
    private SessionService sessionService;

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

    private CreateSessionCommand createTestCreateSessionCommand() {
        return new CreateSessionCommand("Title session command", "Session command description",
                "Session command motivation", "IN_PERSON", "OFFERED", true,
                LocalDateTime.now());
    }

    // =====================================================
    // CREATE SESSION TESTS
    // =====================================================

    @Test
    void shouldCreateSessionSuccessfully() {
        // ARRANGE
        CreateSessionCommand command = createTestCreateSessionCommand();
        Session expectedSession = createTestSession();

        // createSession() always ends by calling sessionRepository.save(...).
        // Whatever we tell the mock to return here is exactly what `result` will be,
        // regardless of what was built internally from `command`.
        when(sessionRepository.save(any(Session.class))).thenReturn(expectedSession);

        // ACT
        Session result = sessionService.createSession(command);

        // ASSERT
        assertNotNull(result);
        assertEquals(expectedSession.getTitle(), result.getTitle());
        assertEquals(expectedSession.getSessionType(), result.getSessionType());
        assertEquals(expectedSession.getSessionOrigin(), result.getSessionOrigin());
        assertEquals(expectedSession.isAllWelcome(), result.isAllWelcome());

        // VERIFY
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    void shouldCreateSessionWithoutOptionalFields() {
        // ARRANGE
        // description, motivation and startDateTime are optional -> null in the command
        CreateSessionCommand command = new CreateSessionCommand(
                "Title only", null, null, "ONLINE", "REQUESTED", false, null);

        // Build an "expected" Session that mirrors a session created without optionals,
        // since `result` will be exactly whatever save() returns.
        Session expectedSession = Session.builder()
                .title("Title only")
                .description(null)
                .motivation(null)
                .sessionType(SessionType.ONLINE)
                .sessionOrigin(SessionOrigin.REQUESTED)
                .allWelcome(false)
                .startDateTime(null)
                .build();

        when(sessionRepository.save(any(Session.class))).thenReturn(expectedSession);

        // ACT
        Session result = sessionService.createSession(command);

        // ASSERT
        assertNotNull(result);
        assertEquals("Title only", result.getTitle());
        assertNull(result.getDescription());
        assertNull(result.getMotivation());
        assertNull(result.getStartDateTime());

        // VERIFY
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    void shouldThrowExceptionWhenSessionTypeIsInvalid() {
        // ARRANGE
        // "INVALID_TYPE" is not a valid SessionType enum value
        CreateSessionCommand command = new CreateSessionCommand(
                "Title", "desc", "motivation", "INVALID_TYPE", "OFFERED", true, LocalDateTime.now());

        // ACT + ASSERT
        // SessionType.fromString(...) throws IllegalArgumentException while building
        // the Session, BEFORE sessionRepository.save() is ever reached.
        assertThrows(IllegalArgumentException.class, () -> sessionService.createSession(command));

        // VERIFY
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void shouldThrowExceptionWhenSessionOriginIsInvalid() {
        // ARRANGE
        // sessionType is valid, but "INVALID_ORIGIN" is not a valid SessionOrigin enum value
        CreateSessionCommand command = new CreateSessionCommand(
                "Title", "desc", "motivation", "IN_PERSON", "INVALID_ORIGIN", true, LocalDateTime.now());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> sessionService.createSession(command));

        // VERIFY
        verify(sessionRepository, never()).save(any(Session.class));
    }

    // =====================================================
    // FIND CANCELLABLE TESTS
    // =====================================================

    @Test
    void shouldFindCancellableSessions() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        Session cancellableSession = createCancellableTestSession(sessionId);

        // findCancellable(title) builds a NEW SessionSearchCriteria internally.
        // Since SessionSearchCriteria doesn't override equals(), that instance will
        // never be == or .equals() to one we create here, so we match with any(...).
        when(sessionRepository.findCancellable(any(SessionSearchCriteria.class)))
                .thenReturn(List.of(cancellableSession));

        // ACT
        List<Session> result = sessionService.findCancellable(null);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sessionId, result.get(0).getId());

        // VERIFY
        verify(sessionRepository, times(1)).findCancellable(any(SessionSearchCriteria.class));
    }

    @Test
    void shouldFindCancellableSessionsFilteredByTitle() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        Session cancellableSession = createCancellableTestSession(sessionId);

        when(sessionRepository.findCancellable(any(SessionSearchCriteria.class)))
                .thenReturn(List.of(cancellableSession));

        // ACT
        List<Session> result = sessionService.findCancellable("Cancellable");

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getTitle().contains("Cancellable"));

        // VERIFY
        // Capture the criteria actually built by findCancellable(...) to check
        // that the title was forwarded correctly.
        ArgumentCaptor<SessionSearchCriteria> captor = ArgumentCaptor.forClass(SessionSearchCriteria.class);
        verify(sessionRepository, times(1)).findCancellable(captor.capture());
        assertEquals("Cancellable", captor.getValue().getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenNoCancellableSessions() {
        // ARRANGE
        when(sessionRepository.findCancellable(any(SessionSearchCriteria.class)))
                .thenReturn(List.of());

        // ACT
        List<Session> result = sessionService.findCancellable(null);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // VERIFY
        verify(sessionRepository, times(1)).findCancellable(any(SessionSearchCriteria.class));
    }

    // =====================================================
    // CANCEL SESSION TESTS
    // =====================================================

    @Test
    void shouldCancelSessionSuccessfully() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        Session session = createCancellableTestSession(sessionId);
        CancelSessionCommand command = new CancelSessionCommand(sessionId, "No assistants enrolled");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // ACT
        sessionService.cancelSession(command);

        // ASSERT
        // cancelSession() calls session.cancel(reason) on the SAME instance we created above,
        // so we can assert directly on `session` to check it was mutated correctly.
        assertNotNull(session.getCancelledAt());
        // Adjust the getter name below if your Session entity uses a different one
        // (e.g. getCancelReason() instead of getCancelledReason()).
        assertEquals("No assistants enrolled", session.getCancelledReason());

        // VERIFY
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void shouldThrowExceptionWhenCancellingNonExistentSession() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        CancelSessionCommand command = new CancelSessionCommand(sessionId, "Reason");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(SessionNotFoundException.class, () -> sessionService.cancelSession(command));

        // VERIFY
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void shouldThrowExceptionWhenCancellingAlreadyCancelledSession() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        Session alreadyCancelledSession = Session.builder()
                .id(sessionId)
                .title("Already cancelled session")
                .sessionType(SessionType.IN_PERSON)
                .sessionOrigin(SessionOrigin.OFFERED)
                .allWelcome(true)
                .startDateTime(LocalDateTime.now().plusDays(1))
                .cancelledAt(LocalDateTime.now().minusDays(1)) // already canceled in the past
                .cancelledReason("Previously cancelled")
                .build();

        CancelSessionCommand command = new CancelSessionCommand(sessionId, "Trying to cancel again");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(alreadyCancelledSession));

        // ACT + ASSERT
        // session.cancel(...) throws SessionAlreadyInactiveException because cancelledAt is already set
        assertThrows(SessionAlreadyInactiveException.class, () -> sessionService.cancelSession(command));

        // VERIFY
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void shouldThrowExceptionWhenCancelReasonIsBlank() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        Session session = createCancellableTestSession(sessionId);
        CancelSessionCommand command = new CancelSessionCommand(sessionId, "");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        // ACT + ASSERT
        // session.cancel("") throws InvalidCancellationReasonException because the reason is blank
        assertThrows(InvalidCancellationReasonException.class, () -> sessionService.cancelSession(command));

        // VERIFY
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, never()).save(any(Session.class));
    }

    // =====================================================
    // SEARCH SESSIONS TESTS
    // =====================================================

    @Test
    void shouldSearchSessions() {
        // ARRANGE
        SessionSearchCriteria criteria = new SessionSearchCriteria(null);
        Pagination pagination = new Pagination(0, 20);
        PageResult<Session> expectedResult = new PageResult<>(
                List.of(createTestSession()), 0, 20, 1, 1);

        when(sessionRepository.search(criteria, pagination)).thenReturn(expectedResult);

        // ACT
        PageResult<Session> result = sessionService.searchSessions(criteria, pagination);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(expectedResult, result);

        // VERIFY
        verify(sessionRepository, times(1)).search(criteria, pagination);
    }

    @Test
    void shouldSearchSessionsWithTitleFilter() {
        // ARRANGE
        SessionSearchCriteria criteria = new SessionSearchCriteria("Test");
        Pagination pagination = new Pagination(0, 20);
        PageResult<Session> expectedResult = new PageResult<>(
                List.of(createTestSession()), 0, 20, 1, 1);

        when(sessionRepository.search(criteria, pagination)).thenReturn(expectedResult);

        // ACT
        PageResult<Session> result = sessionService.searchSessions(criteria, pagination);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        // VERIFY
        verify(sessionRepository, times(1)).search(criteria, pagination);
    }

    // =====================================================
    // ADD ASSISTANT TESTS
    // =====================================================

    @Test
    void shouldAddAssistantSuccessfully() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        Session session = createCancellableTestSession(sessionId);
        AddAssistantCommand command = new AddAssistantCommand(sessionId, "12345678Z");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(personLookupPort.findIdByDni("12345678Z")).thenReturn(Optional.of(personId));

        // ACT
        sessionService.addAssistant(command);

        // ASSERT + VERIFY
        // addAssistant() builds a new Assistant internally and passes it to
        // assistantRepository.add(...). An ArgumentCaptor lets us "catch" that object
        // to inspect its values, since add() doesn't return anything we can check directly.
        ArgumentCaptor<Assistant> captor = ArgumentCaptor.forClass(Assistant.class);
        verify(assistantRepository, times(1)).add(captor.capture());

        Assistant savedAssistant = captor.getValue();
        assertEquals(sessionId, savedAssistant.getSessionId());
        assertEquals(personId, savedAssistant.getPersonId());
        assertTrue(savedAssistant.isRegistered());
        assertFalse(savedAssistant.isAttended());

        verify(sessionRepository, times(1)).findById(sessionId);
        verify(personLookupPort, times(1)).findIdByDni("12345678Z");
    }

    @Test
    void shouldThrowExceptionWhenAddingAssistantToNonExistentSession() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        AddAssistantCommand command = new AddAssistantCommand(sessionId, "12345678Z");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(SessionNotFoundException.class, () -> sessionService.addAssistant(command));

        // VERIFY
        verify(sessionRepository, times(1)).findById(sessionId);
        // Since the session doesn't exist, we should never even try to look up the person
        verify(personLookupPort, never()).findIdByDni(any());
        verify(assistantRepository, never()).add(any(Assistant.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingAssistantWithUnknownDni() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        Session session = createCancellableTestSession(sessionId);
        AddAssistantCommand command = new AddAssistantCommand(sessionId, "00000099F");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(personLookupPort.findIdByDni("00000099F")).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(PersonNotFoundException.class, () -> sessionService.addAssistant(command));

        // VERIFY
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(personLookupPort, times(1)).findIdByDni("00000099F");
        verify(assistantRepository, never()).add(any(Assistant.class));
    }

    // =====================================================
    // REMOVE ASSISTANT TESTS
    // =====================================================

    @Test
    void shouldRemoveAssistantSuccessfully() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        RemoveAssistantCommand command = new RemoveAssistantCommand(sessionId, "12345678Z");

        // This represents the existing row in the `assistants` table for this session
        Assistant existingAssistant = Assistant.builder()
                .sessionId(sessionId)
                .personId(personId)
                .registered(true)
                .attended(false)
                .build();

        when(personLookupPort.findIdByDni("12345678Z")).thenReturn(Optional.of(personId));
        when(assistantRepository.findBySessionId(sessionId)).thenReturn(List.of(existingAssistant));

        // ACT
        sessionService.removeAssistant(command);

        // VERIFY
        verify(personLookupPort, times(1)).findIdByDni("12345678Z");
        verify(assistantRepository, times(1)).findBySessionId(sessionId);
        verify(assistantRepository, times(1)).remove(sessionId, personId);
    }

    @Test
    void shouldThrowExceptionWhenRemovingAssistantWithUnknownDni() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        RemoveAssistantCommand command = new RemoveAssistantCommand(sessionId, "00000099F");

        when(personLookupPort.findIdByDni("00000099F")).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(PersonNotFoundException.class, () -> sessionService.removeAssistant(command));

        // VERIFY
        verify(personLookupPort, times(1)).findIdByDni("00000099F");
        // We should fail BEFORE checking the assistants list or calling remove()
        verify(assistantRepository, never()).findBySessionId(any(UUID.class));
        verify(assistantRepository, never()).remove(any(UUID.class), any(UUID.class));
    }

    @Test
    void shouldThrowExceptionWhenRemovingAssistantNotInSession() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        UUID personId = UUID.randomUUID();
        RemoveAssistantCommand command = new RemoveAssistantCommand(sessionId, "12345678Z");

        when(personLookupPort.findIdByDni("12345678Z")).thenReturn(Optional.of(personId));
        // The person exists, but is NOT registered as an assistant of this session
        when(assistantRepository.findBySessionId(sessionId)).thenReturn(List.of());

        // ACT + ASSERT
        assertThrows(AssistantNotFoundException.class, () -> sessionService.removeAssistant(command));

        // VERIFY
        verify(personLookupPort, times(1)).findIdByDni("12345678Z");
        verify(assistantRepository, times(1)).findBySessionId(sessionId);
        verify(assistantRepository, never()).remove(any(UUID.class), any(UUID.class));
    }

    // =====================================================
    // ADD ADVISORS TESTS
    // =====================================================

    @Test
    void shouldAddAdvisorSuccessfully() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        UUID advisorId = UUID.randomUUID();
        Session session = createCancellableTestSession(sessionId);
        AddAdvisorToSessionCommand command = new AddAdvisorToSessionCommand(sessionId, "12345678Z");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(advisorLookupPort.findIdByDni("12345678Z")).thenReturn(Optional.of(advisorId));

        // ACT
        sessionService.addAdvisor(command);

        // ASSERT + VERIFY
        ArgumentCaptor<AdvisorInSession> captor = ArgumentCaptor.forClass(AdvisorInSession.class);
        verify(advisorInSessionRepository, times(1)).add(captor.capture());

        AdvisorInSession savedAdvisor = captor.getValue();
        assertEquals(sessionId, savedAdvisor.getSessionId());
        assertEquals(advisorId, savedAdvisor.getAdvisorId());

        verify(sessionRepository, times(1)).findById(sessionId);
        verify(advisorLookupPort, times(1)).findIdByDni("12345678Z");
    }

    @Test
    void shouldThrowExceptionWhenAddingAdvisorToNonExistingSession() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        AddAdvisorToSessionCommand command = new AddAdvisorToSessionCommand(sessionId, "12345678Z");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(SessionNotFoundException.class, () -> sessionService.addAdvisor(command));

        // VERIFY
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(advisorLookupPort, never()).findIdByDni(any());
        verify(advisorInSessionRepository, never()).add(any(AdvisorInSession.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingAdvisorWithUnknownDni() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        Session session = createCancellableTestSession(sessionId);
        AddAdvisorToSessionCommand command = new AddAdvisorToSessionCommand(sessionId, "00000099F");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(advisorLookupPort.findIdByDni("00000099F")).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(PersonNotFoundException.class, () -> sessionService.addAdvisor(command));

        // VERIFY
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(advisorLookupPort, times(1)).findIdByDni("00000099F");
        verify(advisorInSessionRepository, never()).add(any(AdvisorInSession.class));
    }

    // =====================================================
    // REMOVE ADVISOR TESTS
    // =====================================================

    @Test
    void shouldRemoveAdvisorSuccessfully() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        UUID advisorId = UUID.randomUUID();
        RemoveAdvisorFromSessionCommand command = new RemoveAdvisorFromSessionCommand(sessionId, "12345678Z");

        // This represents the existing row in the table
        AdvisorInSession advisor = AdvisorInSession.builder()
                .sessionId(sessionId)
                .advisorId(advisorId)
                .build();

        when(advisorLookupPort.findIdByDni("12345678Z")).thenReturn(Optional.of(advisorId));
        when(advisorInSessionRepository.findBySessionId(sessionId)).thenReturn(List.of(advisor));

        // ACT
        sessionService.removeAdvisor(command);

        // VERIFY
        verify(advisorLookupPort, times(1)).findIdByDni("12345678Z");
        verify(advisorInSessionRepository, times(1)).findBySessionId(sessionId);
        verify(advisorInSessionRepository, times(1)).remove(sessionId, advisorId);
    }

    @Test
    void shouldThrowExceptionWhenRemovingAdvisorWithUnknownDni() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        RemoveAdvisorFromSessionCommand command = new RemoveAdvisorFromSessionCommand(sessionId, "00000099F");

        when(advisorLookupPort.findIdByDni("00000099F")).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(PersonNotFoundException.class, () -> sessionService.removeAdvisor(command));

        // VERIFY
        verify(advisorLookupPort, times(1)).findIdByDni("00000099F");
        verify(advisorInSessionRepository, never()).findBySessionId(any(UUID.class));
        verify(advisorInSessionRepository, never()).remove(any(UUID.class), any(UUID.class));

    }

    @Test
    void shouldThrowExceptionWhenRemovingAdvisorNotInSession() {
        // ARRANGE
        UUID sessionId = UUID.randomUUID();
        UUID advisorId = UUID.randomUUID();
        RemoveAdvisorFromSessionCommand command = new RemoveAdvisorFromSessionCommand(sessionId, "12345678Z");

        when(advisorLookupPort.findIdByDni("12345678Z")).thenReturn(Optional.of(advisorId));
        when(advisorInSessionRepository.findBySessionId(sessionId)).thenReturn(List.of());

        // ACT + ASSERT
        assertThrows(AdvisorInSessionNotFoundException.class, () -> sessionService.removeAdvisor(command));

        // VERIFY
        verify(advisorLookupPort, times(1)).findIdByDni("12345678Z");
        verify(advisorInSessionRepository, times(1)).findBySessionId(sessionId);
        verify(advisorInSessionRepository, never()).remove(any(UUID.class), any(UUID.class));
    }
}