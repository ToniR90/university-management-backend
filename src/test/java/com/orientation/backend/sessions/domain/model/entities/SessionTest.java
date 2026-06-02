package com.orientation.backend.sessions.domain.model.entities;

import com.orientation.backend.sessions.domain.model.enums.SessionOrigin;
import com.orientation.backend.sessions.domain.model.enums.SessionType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    // ========== Helper Methods ==========

    private Session createSession() {
        return Session.builder()
                .title("Test Session")
                .sessionType(SessionType.IN_PERSON)
                .sessionOrigin(SessionOrigin.OFFERED)
                .build();
    }

    private Session createFullSession() {
        return Session.builder()
                .id(UUID.randomUUID())
                .title("Full Test Session")
                .description("Test description")
                .motivation("Test motivation")
                .sessionType(SessionType.ONLINE)
                .sessionOrigin(SessionOrigin.REQUESTED)
                .allWelcome(true)
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(1))
                .infoSentAt(LocalDateTime.now().minusDays(1))
                .score(8.5)
                .summary("Test summary")
                .build();
    }

    // ========== Builder Tests ==========

    @Test
    void shouldCreateSessionWithRequiredFields() {
        Session session = createSession();

        assertNotNull(session);
        assertEquals("Test Session", session.getTitle());
        assertEquals(SessionType.IN_PERSON, session.getSessionType());
        assertEquals(SessionOrigin.OFFERED, session.getSessionOrigin());
    }

    @Test
    void shouldCreateFullSession() {
        Session session = createFullSession();

        assertNotNull(session);
        assertNotNull(session.getId());
        assertEquals("Full Test Session", session.getTitle());
        assertEquals("Test description", session.getDescription());
        assertEquals("Test motivation", session.getMotivation());
        assertEquals(SessionType.ONLINE, session.getSessionType());
        assertEquals(SessionOrigin.REQUESTED, session.getSessionOrigin());
        assertTrue(session.isAllWelcome());
        assertNotNull(session.getStartDateTime());
        assertNotNull(session.getEndDateTime());
        assertNotNull(session.getInfoSentAt());
        assertEquals(8.5, session.getScore());
        assertEquals("Test summary", session.getSummary());
    }

    @Test
    void shouldThrowExceptionForNullTitle() {
        assertThrows(NullPointerException.class, () ->
                Session.builder()
                        .title(null)
                        .sessionType(SessionType.IN_PERSON)
                        .sessionOrigin(SessionOrigin.OFFERED)
                        .build()
        );
    }

    @Test
    void shouldThrowExceptionForNullSessionType() {
        assertThrows(NullPointerException.class, () ->
                Session.builder()
                        .title("Test Session")
                        .sessionType(null)
                        .sessionOrigin(SessionOrigin.OFFERED)
                        .build()
        );
    }

    @Test
    void shouldThrowExceptionForNullSessionOrigin() {
        assertThrows(NullPointerException.class, () ->
                Session.builder()
                        .title("Test Session")
                        .sessionType(SessionType.IN_PERSON)
                        .sessionOrigin(null)
                        .build()
        );
    }

    // ========== Optional Fields Tests ==========

    @Test
    void shouldAllowNullDescription() {
        Session session = Session.builder()
                .title("Test Session")
                .sessionType(SessionType.IN_PERSON)
                .sessionOrigin(SessionOrigin.OFFERED)
                .description(null)
                .build();

        assertNull(session.getDescription());
    }

    @Test
    void shouldAllowNullStartDateTime() {
        Session session = createSession();

        assertNull(session.getStartDateTime());
    }

    @Test
    void shouldAllowNullCancelledAt() {
        Session session = createSession();

        assertNull(session.getCancelledAt());
        assertNull(session.getCancelledReason());
    }

    @Test
    void shouldSetCancelledAtAndReason() {
        LocalDateTime cancelledAt = LocalDateTime.now();
        Session session = Session.builder()
                .title("Test Session")
                .sessionType(SessionType.IN_PERSON)
                .sessionOrigin(SessionOrigin.OFFERED)
                .cancelledAt(cancelledAt)
                .cancelledReason("No attendees")
                .build();

        assertEquals(cancelledAt, session.getCancelledAt());
        assertEquals("No attendees", session.getCancelledReason());
    }

    @Test
    void shouldDefaultAllWelcomeToFalse() {
        Session session = createSession();

        assertFalse(session.isAllWelcome());
    }
}