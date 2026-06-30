package com.orientation.backend.sessions.infraestructure.persistance.repositories;

import com.orientation.backend.BaseIntegrationTest;
import com.orientation.backend.sessions.domain.model.entities.AdvisorInSession;
import com.orientation.backend.sessions.infraestructure.persistance.entities.SessionJpaEntity;
import com.orientation.backend.sessions.domain.model.enums.SessionOrigin;
import com.orientation.backend.sessions.domain.model.enums.SessionType;
import com.orientation.backend.users.infrastructure.persistence.entities.AdvisorJpaEntity;
import com.orientation.backend.users.infrastructure.persistence.repositories.SpringDataAdvisorRepository;
import com.orientation.backend.users.infrastructure.persistence.repositories.SpringDataPersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AdvisorInSessionRepositoryImpl.class)
class AdvisorInSessionRepositoryImplTest extends BaseIntegrationTest {

    @Autowired
    private AdvisorInSessionRepositoryImpl advisorInSessionRepository;

    @Autowired
    private SpringDataSessionRepository sessionJpaRepository;

    @Autowired
    private SpringDataPersonRepository personJpaRepository;

    @Autowired
    private SpringDataAdvisorRepository advisorJpaRepository;

    // ========== Helper Methods ==========

    /**
     * Creates and persists a minimal Session directly via JPA,
     * needed because `advisors_in_session.session_id` has a FK constraint to `session`.
     */
    private UUID createPersistedSession() {
        SessionJpaEntity session = new SessionJpaEntity();
        session.setTitle("Test Session for Advisor");
        session.setSessionType(SessionType.ONLINE);
        session.setSessionOrigin(SessionOrigin.OFFERED);

        SessionJpaEntity saved = sessionJpaRepository.save(session);
        return saved.getId();
    }

    /**
     * Creates and persists a Person + Advisor directly via JPA,
     * needed because `advisors_in_session.advisor_id` has a FK constraint to `advisor`.
     * Returns the ADVISOR's own id (advisor.id), not the person_id, since that's
     * what the `advisors_in_session.advisor_id` foreign key actually points to.
     */
    private UUID createPersistedAdvisor(String dni) {
        AdvisorJpaEntity advisor = new AdvisorJpaEntity();
        advisor.setDni(dni);
        advisor.setName("Test");
        advisor.setSurname("Advisor");
        advisor.setEmail(dni + "@example.com");
        advisor.setActive(true);
        advisor.setRgpdConsentStatus("PENDING");

        AdvisorJpaEntity saved = advisorJpaRepository.save(advisor);
        return saved.getId();
    }

    private AdvisorInSession createTestAdvisorInSession(UUID sessionId, UUID advisorId) {
        return AdvisorInSession.builder()
                .sessionId(sessionId)
                .advisorId(advisorId)
                .build();
    }

    // =====================================================
    // ADD TESTS
    // =====================================================

    @Test
    void shouldAddAdvisorToSessionSuccessfully() {
        // ARRANGE
        UUID sessionId = createPersistedSession();
        UUID advisorId = createPersistedAdvisor("00000001R");
        AdvisorInSession advisorInSession = createTestAdvisorInSession(sessionId, advisorId);

        // ACT
        AdvisorInSession saved = advisorInSessionRepository.add(advisorInSession);

        // ASSERT
        assertThat(saved).isNotNull();
        assertThat(saved.getSessionId()).isEqualTo(sessionId);
        assertThat(saved.getAdvisorId()).isEqualTo(advisorId);
    }

    // =====================================================
    // FIND BY SESSION ID TESTS
    // =====================================================

    @Test
    void shouldFindAdvisorsBySessionId() {
        // ARRANGE
        UUID sessionId = createPersistedSession();
        UUID advisorId = createPersistedAdvisor("00000002W");
        AdvisorInSession advisorInSession = createTestAdvisorInSession(sessionId, advisorId);
        advisorInSessionRepository.add(advisorInSession);

        // ACT
        List<AdvisorInSession> found = advisorInSessionRepository.findBySessionId(sessionId);

        // ASSERT
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getSessionId()).isEqualTo(sessionId);
        assertThat(found.get(0).getAdvisorId()).isEqualTo(advisorId);
    }

    @Test
    void shouldReturnEmptyListWhenSessionHasNoAdvisors() {
        // ARRANGE
        UUID sessionId = createPersistedSession();

        // ACT
        List<AdvisorInSession> found = advisorInSessionRepository.findBySessionId(sessionId);

        // ASSERT
        assertThat(found).isEmpty();
    }

    // =====================================================
    // REMOVE TESTS
    // =====================================================

    @Test
    void shouldRemoveAdvisorFromSessionSuccessfully() {
        // ARRANGE
        UUID sessionId = createPersistedSession();
        UUID advisorId = createPersistedAdvisor("00000003A");
        AdvisorInSession advisorInSession = createTestAdvisorInSession(sessionId, advisorId);
        advisorInSessionRepository.add(advisorInSession);

        // ACT
        advisorInSessionRepository.remove(sessionId, advisorId);

        // ASSERT
        List<AdvisorInSession> found = advisorInSessionRepository.findBySessionId(sessionId);
        assertThat(found).isEmpty();
    }
}