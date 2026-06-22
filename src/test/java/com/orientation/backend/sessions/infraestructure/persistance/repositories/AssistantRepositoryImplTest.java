package com.orientation.backend.sessions.infraestructure.persistance.repositories;

import com.orientation.backend.BaseIntegrationTest;
import com.orientation.backend.sessions.domain.model.entities.Assistant;
import com.orientation.backend.sessions.domain.model.enums.SessionOrigin;
import com.orientation.backend.sessions.domain.model.enums.SessionType;
import com.orientation.backend.sessions.infraestructure.persistance.entities.SessionJpaEntity;
import com.orientation.backend.users.infrastructure.persistence.entities.PersonJpaEntity;
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
@Import(AssistantRepositoryImpl.class)
class AssistantRepositoryImplTest extends BaseIntegrationTest {

    @Autowired
    private AssistantRepositoryImpl assistantRepository;

    @Autowired
    private SpringDataSessionRepository sessionJpaRepository;

    @Autowired
    private SpringDataPersonRepository personJpaRepository;

    // ========== Helper Methods ==========

    /**
     * Creates and persists a minimal Session directly via JPA,
     * needed because `assistants.session_id` has a FK constraint to `session`.
     */
    private UUID createPersistedSession() {
        SessionJpaEntity session = new SessionJpaEntity();
        session.setTitle("Test Session for Assistant");
        session.setSessionType(SessionType.ONLINE);
        session.setSessionOrigin(SessionOrigin.OFFERED);

        SessionJpaEntity saved = sessionJpaRepository.save(session);
        return saved.getId();
    }

    /**
     * Creates and persists a minimal Person directly via JPA,
     * needed because `assistants.person_id` has a FK constraint to `person`.
     */
    private UUID createPersistedPerson() {
        PersonJpaEntity person = new PersonJpaEntity();
        person.setDni("12345678Z");
        person.setName("Test");
        person.setSurname("Person");
        person.setEmail("testperson@example.com");
        person.setActive(true);
        person.setRgpdConsentStatus("PENDING");

        PersonJpaEntity saved = personJpaRepository.save(person);
        return saved.getId();
    }

    private Assistant createTestAssistant(UUID sessionId, UUID personId) {
        return Assistant.builder()
                .sessionId(sessionId)
                .personId(personId)
                .registered(true)
                .attended(false)
                .build();
    }

    // =====================================================
    // ADD TESTS
    // =====================================================

    @Test
    void shouldAddAssistantSuccessfully() {
        // ARRANGE
        UUID sessionId = createPersistedSession();
        UUID personId = createPersistedPerson();
        Assistant assistant = createTestAssistant(sessionId, personId);

        // ACT
        Assistant saved = assistantRepository.add(assistant);

        // ASSERT
        assertThat(saved).isNotNull();
        assertThat(saved.getSessionId()).isEqualTo(sessionId);
        assertThat(saved.getPersonId()).isEqualTo(personId);
        assertThat(saved.isRegistered()).isTrue();
        assertThat(saved.isAttended()).isFalse();
    }

    // =====================================================
    // FIND BY SESSION ID TESTS
    // =====================================================

    @Test
    void shouldFindAssistantsBySessionId() {
        // ARRANGE
        UUID sessionId = createPersistedSession();
        UUID personId = createPersistedPerson();
        Assistant assistant = createTestAssistant(sessionId, personId);
        assistantRepository.add(assistant);

        // ACT
        List<Assistant> found = assistantRepository.findBySessionId(sessionId);

        // ASSERT
        assertThat(found).hasSize(1);
        assertThat(found.getFirst().getSessionId()).isEqualTo(sessionId);
        assertThat(found.getFirst().getPersonId()).isEqualTo(personId);
    }

    @Test
    void shouldReturnEmptyListWhenSessionHasNoAssistants() {
        // ARRANGE
        UUID sessionId = createPersistedSession();

        // ACT
        List<Assistant> found = assistantRepository.findBySessionId(sessionId);

        // ASSERT
        assertThat(found).isEmpty();
    }

    // =====================================================
    // REMOVE TESTS
    // =====================================================

    @Test
    void shouldRemoveAssistantSuccessfully() {
        // ARRANGE
        UUID sessionId = createPersistedSession();
        UUID personId = createPersistedPerson();
        Assistant assistant = createTestAssistant(sessionId, personId);
        assistantRepository.add(assistant);

        // ACT
        assistantRepository.remove(sessionId, personId);

        // ASSERT
        List<Assistant> found = assistantRepository.findBySessionId(sessionId);
        assertThat(found).isEmpty();
    }
}