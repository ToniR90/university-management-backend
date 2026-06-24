package com.orientation.backend.users.application.services;

import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.application.commands.advisors.CreateAdvisorCommand;
import com.orientation.backend.users.application.exceptions.advisors.CreatedAdvisorException;
import com.orientation.backend.users.domain.model.entities.Advisor;
import com.orientation.backend.users.domain.model.query.AdvisorSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.model.valueobjects.Email;
import com.orientation.backend.users.domain.model.valueobjects.FullName;
import com.orientation.backend.users.domain.model.valueobjects.Phone;
import com.orientation.backend.users.domain.repository.AdvisorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdvisorServiceTest {

    @Mock
    private AdvisorRepository advisorRepository;

    @InjectMocks
    private AdvisorService advisorService;

    // ========== Helper Methods ==========

    private Advisor createTestAdvisor(String dni, String email) {
        return Advisor.builder()
                .id(UUID.randomUUID())
                .dni(Dni.of(dni))
                .fullName(FullName.of("test_advisor_name", "test_advisor_surname"))
                .email(Optional.of(Email.of(email)))
                .phone(Optional.of(Phone.of("+34612345678")))
                .build();
    }

    private CreateAdvisorCommand createTestAdvisorCommand(String dni) {
        return new CreateAdvisorCommand(
                dni,
                "test_advisor_name",
                "test_advisor_surname",
                "test@email.com",
                "+34612345678"
        );
    }

    // =====================================================
    // CREATE ADVISOR TESTS
    // =====================================================

    @Test
    void shouldCreateAdvisorSuccessfully() {
        // ARRANGE
        CreateAdvisorCommand command = createTestAdvisorCommand("00000000T");

        Advisor expectedAdvisor = createTestAdvisor("00000000T", "test@email.com");

        when(advisorRepository.existsByDni(any(Dni.class))).thenReturn(false);
        when(advisorRepository.save(any(Advisor.class))).thenReturn(expectedAdvisor);

        // ACT
        Advisor result = advisorService.createAdvisor(command);

        // ASSERT
        assertNotNull(result);
        assertEquals("00000000T", result.getDni().getValue());
        assertEquals("test_advisor_name", result.getFullName().getName());
        assertEquals("test_advisor_surname", result.getFullName().getSurname());

        assertTrue(result.getEmail().isPresent());
        assertEquals("test@email.com", result.getEmail().get().getValue());

        assertTrue(result.getPhone().isPresent());
        assertEquals("+34612345678", result.getPhone().get().getValue());

        // VERIFY
        verify(advisorRepository, times(1))
                .existsByDni(any(Dni.class));

        verify(advisorRepository, times(1))
                .save(any(Advisor.class));
    }

    @Test
    void shouldCreateAdvisorWithoutOptionalFields() {
        // ARRANGE
        CreateAdvisorCommand command = new CreateAdvisorCommand(
                "00000002W",
                "test_advisor_name",
                "test_advisor_surname",
                "test_1@email.com",
                null
        );

        Advisor expectedAdvisor = Advisor.builder()
                .id(UUID.randomUUID())
                .dni(Dni.of("00000002W"))
                .fullName(FullName.of("test_advisor_name", "test_advisor_surname"))
                .email(Optional.of(Email.of("test@email.com")))
                .phone(Optional.empty())
                .build();

        when(advisorRepository.existsByDni(any(Dni.class))).thenReturn(false);
        when(advisorRepository.save(any(Advisor.class))).thenReturn(expectedAdvisor);

        // ACT
        Advisor result = advisorService.createAdvisor(command);

        // ASSERT
        assertTrue(result.getPhone().isEmpty());

        // VERIFY
        verify(advisorRepository, times(1))
                .existsByDni(any(Dni.class));

        verify(advisorRepository, times(1))
                .save(any(Advisor.class));
    }

    @Test
    void shouldThrowExceptionWhenDniAlreadyExists() {
        // ARRANGE
        CreateAdvisorCommand command = createTestAdvisorCommand("00000001R");

        when(advisorRepository.existsByDni(any(Dni.class))).thenReturn(true);

        // ACT + ASSERT
        assertThrows(CreatedAdvisorException.class, () -> advisorService.createAdvisor(command));

        // VERIFY
        verify(advisorRepository, times(1))
                .existsByDni(any(Dni.class));

        verify(advisorRepository, never())
                .save(any(Advisor.class));
    }

    @Test
    void shouldThrowExceptionWhenDniFormatIsInvalid() {
        // ARRANGE
        CreateAdvisorCommand command = createTestAdvisorCommand("12345678A");

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> advisorService.createAdvisor(command));

        // VERIFY
        verify(advisorRepository, never())
                .existsByDni(any(Dni.class));

        verify(advisorRepository, never())
                .save(any(Advisor.class));
    }

    // =====================================================
    // SEARCH ADVISORS TESTS
    // =====================================================

    @Test
    void shouldSearchAdvisors() {
        // ARRANGE
        PageResult<Advisor> expectedResult = new PageResult<>(
                List.of(createTestAdvisor("00000015S", "test@email.com")),
                0, 20, 1, 1
        );
        AdvisorSearchCriteria criteria = new AdvisorSearchCriteria(null, null);
        Pagination pagination = new Pagination(0, 20);

        when(advisorRepository.search(criteria, pagination)).thenReturn(expectedResult);

        // ACT
        PageResult<Advisor> result = advisorService.searchAdvisors(criteria, pagination);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(expectedResult, result);

        // VERIFY
        verify(advisorRepository).search(criteria, pagination);
    }

    @Test
    void shouldSearchAdvisorsWithNameFilter() {
        // ARRANGE
        PageResult<Advisor> expectedResult = new PageResult<>(
                List.of(createTestAdvisor("00000015S", "test@email.com")),
                0, 20, 1, 1
        );
        AdvisorSearchCriteria criteria = new AdvisorSearchCriteria("test_advisor_name", null);
        Pagination pagination = new Pagination(0, 20);

        when(advisorRepository.search(criteria, pagination)).thenReturn(expectedResult);

        // ACT
        PageResult<Advisor> result = advisorService.searchAdvisors(criteria, pagination);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(expectedResult, result);

        // VERIFY
        verify(advisorRepository).search(criteria, pagination);
    }
}