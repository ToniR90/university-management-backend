package com.orientation.backend.sessions.infraestructure.persistance.repositories;

import com.orientation.backend.BaseIntegrationTest;
import com.orientation.backend.sessions.domain.model.entities.Session;
import com.orientation.backend.sessions.domain.model.enums.SessionOrigin;
import com.orientation.backend.sessions.domain.model.enums.SessionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SessionRepositoryImpl.class)
class SessionRepositoryImplTest extends BaseIntegrationTest {

    @Autowired
    private SessionRepositoryImpl sessionRepository;

    private Session createSession() {
        return Session.builder()
                .title("Test Session")
                .sessionType(SessionType.IN_PERSON)
                .sessionOrigin(SessionOrigin.OFFERED)
                .build();
    }

    private Session createFullSession() {
        return Session.builder()
                .title("Full Test Session")
                .description("Test description")
                .motivation("Test motivation")
                .sessionType(SessionType.ONLINE)
                .sessionOrigin(SessionOrigin.REQUESTED)
                .allWelcome(true)
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(1))
                .score(8.5)
                .summary("Test summary")
                .build();
    }

    // ========== CRUD Tests ==========

    @Test
    void shouldSaveSession() {
        Session session = createSession();

        Session saved = sessionRepository.save(session);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Test Session");
        assertThat(saved.getSessionType()).isEqualTo(SessionType.IN_PERSON);
        assertThat(saved.getSessionOrigin()).isEqualTo(SessionOrigin.OFFERED);
    }

    @Test
    void shouldSaveFullSession() {
        Session session = createFullSession();

        Session saved = sessionRepository.save(session);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Full Test Session");
        assertThat(saved.getDescription()).isEqualTo("Test description");
        assertThat(saved.getSessionType()).isEqualTo(SessionType.ONLINE);
        assertThat(saved.getSessionOrigin()).isEqualTo(SessionOrigin.REQUESTED);
        assertThat(saved.isAllWelcome()).isTrue();
        assertThat(saved.getScore()).isEqualTo(8.5);
    }

    @Test
    void shouldFindAllSessions() {
        sessionRepository.save(createSession());
        sessionRepository.save(createFullSession());

        List<Session> all = sessionRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void shouldReturnEmptyListWhenNoSessions() {
        List<Session> all = sessionRepository.findAll();

        assertThat(all).isEmpty();
    }

    @Test
    void shouldSaveCancelledSession() {
        Session session = Session.builder()
                .title("Cancelled Session")
                .sessionType(SessionType.IN_PERSON)
                .sessionOrigin(SessionOrigin.OFFERED)
                .cancelledAt(LocalDateTime.now())
                .cancelledReason("No attendees")
                .build();

        Session saved = sessionRepository.save(session);

        assertThat(saved).isNotNull();
        assertThat(saved.getCancelledAt()).isNotNull();
        assertThat(saved.getCancelledReason()).isEqualTo("No attendees");
    }

    @Test
    void shouldUpdateSession() {
        Session session = createSession();
        Session saved = sessionRepository.save(session);

        Session updated = Session.builder()
                .id(saved.getId())
                .title("Updated Session")
                .sessionType(SessionType.ONLINE)
                .sessionOrigin(SessionOrigin.REQUESTED)
                .summary("Updated summary")
                .build();

        Session result = sessionRepository.save(updated);

        assertThat(result.getTitle()).isEqualTo("Updated Session");
        assertThat(result.getSummary()).isEqualTo("Updated summary");
    }
}