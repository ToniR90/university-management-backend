package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.BaseIntegrationTest;
import com.orientation.backend.users.domain.model.entities.Collaborator;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.model.valueobjects.Email;
import com.orientation.backend.users.domain.model.valueobjects.FullName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(CollaboratorRepositoryImpl.class)
class CollaboratorRepositoryImplTest extends BaseIntegrationTest {

    @Autowired
    private CollaboratorRepositoryImpl collaboratorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Collaborator createCollaborator(String dni, String email) {
        return Collaborator.builder()
                .dni(Dni.of(dni))
                .fullName(FullName.of("Test", "Collaborator"))
                .email(Optional.of(Email.of(email)))
                .external(false)
                .build();
    }

    // ========== CRUD Tests ==========

    @Test
    void shouldSaveCollaborator() {
        Collaborator collaborator = createCollaborator("00000013J", "collaborator1@mail.com");

        Collaborator saved = collaboratorRepository.save(collaborator);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDni().getValue()).isEqualTo("00000013J");
    }

    @Test
    void shouldFindCollaboratorByDni() {
        Collaborator collaborator = createCollaborator("00000014Z", "collaborator2@mail.com");
        collaboratorRepository.save(collaborator);

        Optional<Collaborator> found = collaboratorRepository.findByDni(Dni.of("00000014Z"));

        assertThat(found).isPresent();
        assertThat(found.get().getDni().getValue()).isEqualTo("00000014Z");
    }

    @Test
    void shouldReturnEmptyWhenDniNotFound() {
        Optional<Collaborator> found = collaboratorRepository.findByDni(Dni.of("00000015S"));

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenDniExists() {
        Collaborator collaborator = createCollaborator("00000016Q", "collaborator4@mail.com");
        collaboratorRepository.save(collaborator);

        boolean exists = collaboratorRepository.existsByDni(Dni.of("00000016Q"));

        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenDniNotExists() {
        boolean exists = collaboratorRepository.existsByDni(Dni.of("00000017V"));

        assertThat(exists).isFalse();
    }

    @Test
    void shouldFindAllActiveCollaborators() {
        Collaborator collaborator1 = createCollaborator("00000018H", "collaborator6@mail.com");
        Collaborator collaborator2 = createCollaborator("00000019L", "collaborator7@mail.com");

        collaboratorRepository.save(collaborator1);
        collaboratorRepository.save(collaborator2);

        List<Collaborator> all = collaboratorRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void shouldSaveExternalCollaborator() {
        Collaborator collaborator = Collaborator.builder()
                .dni(Dni.of("00000020C"))
                .fullName(FullName.of("External", "Collaborator"))
                .email(Optional.of(Email.of("external@mail.com")))
                .external(true)
                .build();

        Collaborator saved = collaboratorRepository.save(collaborator);

        assertThat(saved).isNotNull();
        assertThat(saved.isExternal()).isTrue();
    }

    // ========== Soft Delete Tests ==========

    @Test
    void shouldNotFindCollaboratorAfterDeactivation() {
        Collaborator collaborator = createCollaborator("00000021K", "collaborator8@mail.com");
        Collaborator saved = collaboratorRepository.save(collaborator);

        saved.deactivate();
        collaboratorRepository.save(saved);

        assertThat(collaboratorRepository.findByDni(Dni.of("00000021K"))).isEmpty();
    }

    @Test
    void shouldNotIncludeDeactivatedInFindAll() {
        Collaborator active = createCollaborator("00000022E", "collaborator9@mail.com");
        Collaborator toDeactivate = createCollaborator("00000023T", "collaborator10@mail.com");

        collaboratorRepository.save(active);
        Collaborator saved = collaboratorRepository.save(toDeactivate);

        saved.deactivate();
        collaboratorRepository.save(saved);

        List<Collaborator> all = collaboratorRepository.findAll();

        assertThat(all).hasSize(1);
        assertThat(all.get(0).getDni().getValue()).isEqualTo("00000022E");
    }

    @Test
    void shouldNotExistByDniAfterDeactivation() {
        Collaborator collaborator = createCollaborator("00000024R", "collaborator11@mail.com");
        Collaborator saved = collaboratorRepository.save(collaborator);

        saved.deactivate();
        collaboratorRepository.save(saved);

        assertThat(collaboratorRepository.existsByDni(Dni.of("00000024R"))).isFalse();
    }

    @Test
    void shouldReuseDniAfterDeactivation() {
        Collaborator collaborator = createCollaborator("00000025W", "collaborator12@mail.com");
        Collaborator saved = collaboratorRepository.save(collaborator);

        saved.deactivate();
        collaboratorRepository.save(saved);

        entityManager.flush();
        entityManager.clear();

        Collaborator newCollaborator = createCollaborator("00000025W", "collaborator12new@mail.com");
        Collaborator newSaved = collaboratorRepository.save(newCollaborator);

        assertThat(newSaved.getId()).isNotNull();
        assertThat(collaboratorRepository.findByDni(Dni.of("00000025W"))).isPresent();
    }
}