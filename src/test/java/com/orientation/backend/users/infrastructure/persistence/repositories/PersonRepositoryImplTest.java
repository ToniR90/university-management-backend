package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.BaseIntegrationTest;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.infrastructure.persistence.entities.PersonJpaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersonRepositoryImpl.class)
class PersonRepositoryImplTest extends BaseIntegrationTest {

    @Autowired
    private PersonRepositoryImpl personRepository;

    @Autowired
    private SpringDataPersonRepository personJpaRepository;

    // ========== Helper Methods ==========

    /**
     * Creates and persists a minimal active Person directly via JPA.
     */
    private UUID createPersistedPerson(String dni) {
        PersonJpaEntity person = new PersonJpaEntity();
        person.setDni(dni);
        person.setName("Test");
        person.setSurname("Person");
        person.setEmail(dni + "@example.com");
        person.setActive(true);
        person.setRgpdConsentStatus("PENDING");

        PersonJpaEntity saved = personJpaRepository.save(person);
        return saved.getId();
    }

    /**
     * Creates and persists an INACTIVE Person directly via JPA,
     * useful for testing that findIdByDni only matches active people.
     */
    private UUID createPersistedInactivePerson(String dni) {
        PersonJpaEntity person = new PersonJpaEntity();
        person.setDni(dni);
        person.setName("Inactive");
        person.setSurname("Person");
        person.setEmail(dni + "@example.com");
        person.setActive(false);
        person.setRgpdConsentStatus("PENDING");

        PersonJpaEntity saved = personJpaRepository.save(person);
        return saved.getId();
    }

    // =====================================================
    // FIND ID BY DNI TESTS
    // =====================================================

    @Test
    void shouldFindIdByDniWhenPersonExistsAndIsActive() {
        // ARRANGE
        String dni = "00000001R";
        UUID personId = createPersistedPerson(dni);

        // ACT
        Optional<UUID> found = personRepository.findIdByDni(Dni.of(dni));

        // ASSERT
        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(personId);
    }

    @Test
    void shouldReturnEmptyWhenDniDoesNotExist() {
        // ARRANGE
        String dni = "00000099F";

        // ACT
        Optional<UUID> found = personRepository.findIdByDni(Dni.of(dni));

        // ASSERT
        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenPersonExistsButIsInactive() {
        // ARRANGE
        String dni = "00000002W";
        UUID personId = createPersistedInactivePerson(dni);

        // ACT
        Optional<UUID> found = personRepository.findIdByDni(Dni.of(dni));

        // ASSERT
        assertThat(found).isEmpty();
        assertThat(personId).isNotNull();
    }
}