package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.BaseIntegrationTest;
import com.orientation.backend.users.domain.model.entities.Advisor;
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
@Import(AdvisorRepositoryImpl.class)
class AdvisorRepositoryImplTest extends BaseIntegrationTest {

    @Autowired
    private AdvisorRepositoryImpl advisorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Advisor createAdvisor(String dni, String email) {
        return Advisor.builder()
                .dni(Dni.of(dni))
                .fullName(FullName.of("Test", "Advisor"))
                .email(Email.of(email))
                .build();
    }

    // ========== CRUD Tests ==========

    @Test
    void shouldSaveAdvisor() {
        Advisor advisor = createAdvisor("00000001R", "advisor1@mail.com");

        Advisor saved = advisorRepository.save(advisor);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDni().getValue()).isEqualTo("00000001R");
    }

    @Test
    void shouldFindAdvisorByDni() {
        Advisor advisor = createAdvisor("00000002W", "advisor2@mail.com");
        advisorRepository.save(advisor);

        Optional<Advisor> found = advisorRepository.findByDni(Dni.of("00000002W"));

        assertThat(found).isPresent();
        assertThat(found.get().getDni().getValue()).isEqualTo("00000002W");
    }

    @Test
    void shouldReturnEmptyWhenDniNotFound() {
        Optional<Advisor> found = advisorRepository.findByDni(Dni.of("00000003A"));

        assertThat(found).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenDniExists() {
        Advisor advisor = createAdvisor("00000004G", "advisor4@mail.com");
        advisorRepository.save(advisor);

        boolean exists = advisorRepository.existsByDni(Dni.of("00000004G"));

        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenDniNotExists() {
        boolean exists = advisorRepository.existsByDni(Dni.of("00000005M"));

        assertThat(exists).isFalse();
    }

    @Test
    void shouldFindAllActiveAdvisors() {
        Advisor advisor1 = createAdvisor("00000006Y", "advisor6@mail.com");
        Advisor advisor2 = createAdvisor("00000007F", "advisor7@mail.com");

        advisorRepository.save(advisor1);
        advisorRepository.save(advisor2);

        List<Advisor> all = advisorRepository.findAll();

        assertThat(all).hasSize(2);
    }

    // ========== Soft Delete Tests ==========

    @Test
    void shouldNotFindAdvisorAfterDeactivation() {
        Advisor advisor = createAdvisor("00000008P", "advisor8@mail.com");
        Advisor saved = advisorRepository.save(advisor);

        saved.deactivate();
        advisorRepository.save(saved);

        assertThat(advisorRepository.findByDni(Dni.of("00000008P"))).isEmpty();
    }

    @Test
    void shouldNotIncludeDeactivatedInFindAll() {
        Advisor active = createAdvisor("00000009D", "advisor9@mail.com");
        Advisor toDeactivate = createAdvisor("00000010X", "advisor10@mail.com");

        advisorRepository.save(active);
        Advisor saved = advisorRepository.save(toDeactivate);

        saved.deactivate();
        advisorRepository.save(saved);

        List<Advisor> all = advisorRepository.findAll();

        assertThat(all).hasSize(1);
        assertThat(all.get(0).getDni().getValue()).isEqualTo("00000009D");
    }

    @Test
    void shouldNotExistByDniAfterDeactivation() {
        Advisor advisor = createAdvisor("00000011B", "advisor11@mail.com");
        Advisor saved = advisorRepository.save(advisor);

        saved.deactivate();
        advisorRepository.save(saved);

        assertThat(advisorRepository.existsByDni(Dni.of("00000011B"))).isFalse();
    }

    @Test
    void shouldReuseDniAfterDeactivation() {
        Advisor advisor = createAdvisor("00000012N", "advisor12@mail.com");
        Advisor saved = advisorRepository.save(advisor);

        saved.deactivate();
        advisorRepository.save(saved);

        entityManager.flush();
        entityManager.clear();

        Advisor newAdvisor = createAdvisor("00000012N", "advisor12new@mail.com");
        Advisor newSaved = advisorRepository.save(newAdvisor);

        assertThat(newSaved.getId()).isNotNull();
        assertThat(advisorRepository.findByDni(Dni.of("00000012N"))).isPresent();
    }
}