package com.orientation.backend.users.application.services;

import com.orientation.backend.shared.domain.model.query.PageResult;
import com.orientation.backend.shared.domain.model.query.Pagination;
import com.orientation.backend.users.application.commands.collaborators.CreateCollaboratorCommand;
import com.orientation.backend.users.application.exceptions.collaborators.CreatedCollaboratorException;
import com.orientation.backend.users.domain.model.entities.Collaborator;
import com.orientation.backend.users.domain.model.entities.Organization;
import com.orientation.backend.users.domain.model.query.CollaboratorSearchCriteria;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.model.valueobjects.Email;
import com.orientation.backend.users.domain.model.valueobjects.FullName;
import com.orientation.backend.users.domain.model.valueobjects.Phone;
import com.orientation.backend.users.domain.repository.CollaboratorRepository;
import com.orientation.backend.users.domain.repository.OrganizationRepository;
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
class CollaboratorServiceTest {

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private CollaboratorService collaboratorService;

    // ========== Helper Methods ==========

    private Collaborator createTestCollaborator(String dni) {
        return Collaborator.builder()
                .id(UUID.randomUUID())
                .dni(Dni.of(dni))
                .fullName(FullName.of("test_collaborator_name", "test_collaborator_surname"))
                .email(Optional.of(Email.of("collaborator@email.com")))
                .phone(Optional.of(Phone.of("+34612345678")))
                .external(false)
                .organization(null)
                .build();
    }

    private Organization createTestOrganization() {
        return Organization.builder()
                .id(UUID.randomUUID())
                .name("test_organization_test")
                .build();
    }

    private CreateCollaboratorCommand createTestCommand(String dni) {
        return new CreateCollaboratorCommand(
                dni,
                "test_collaborator_name",
                "test_collaborator_surname",
                "collaborator@email.com",
                "+34612345678",
                false,
                null
        );
    }

    // =====================================================
    // CREATE COLLABORATOR TESTS
    // =====================================================

    @Test
    void shouldCreateCollaboratorSuccessfully() {
        // ARRANGE
        CreateCollaboratorCommand command = createTestCommand("00000000T");

        Collaborator expectedCollaborator = createTestCollaborator("00000000T");

        when(collaboratorRepository.existsByDni(any(Dni.class))).thenReturn(false);
        when(collaboratorRepository.save(any(Collaborator.class))).thenReturn(expectedCollaborator);

        // ACT
        Collaborator result = collaboratorService.createCollaborator(command);

        // ASSERT
        assertNotNull(result);
        assertEquals("00000000T", result.getDni().getValue());
        assertEquals("test_collaborator_name", result.getFullName().getName());
        assertEquals("test_collaborator_surname", result.getFullName().getSurname());

        assertTrue(result.getEmail().isPresent());
        assertEquals("collaborator@email.com", result.getEmail().get().getValue());

        assertTrue(result.getPhone().isPresent());
        assertEquals("+34612345678", result.getPhone().get().getValue());

        // VERIFY
        verify(collaboratorRepository, times(1))
                .existsByDni(any(Dni.class));
        verify(collaboratorRepository, times(1))
                .save(any(Collaborator.class));
    }

    @Test
    void shouldCreateCollaboratorWithoutOptionalFields() {
        // ARRANGE
        CreateCollaboratorCommand command = new CreateCollaboratorCommand(
                "00000002W",
                "test_collaborator_test",
                "test_collaborator_surname",
                "collaborator@email.com",
                null,
                false,
                null
        );

        Collaborator expectedCollaborator = Collaborator.builder()
                .id(UUID.randomUUID())
                .dni(Dni.of("00000002W"))
                .fullName(FullName.of("test_collaborator_name", "test_collaborator_surname"))
                .email(Optional.of(Email.of("collaborator@email.com")))
                .phone(Optional.empty())
                .external(false)
                .organization(null)
                .build();

        when(collaboratorRepository.existsByDni(any(Dni.class))).thenReturn(false);
        when(collaboratorRepository.save(any(Collaborator.class))).thenReturn(expectedCollaborator);

        // ACT
        Collaborator result = collaboratorService.createCollaborator(command);

        // ASSERT
        assertTrue(result.getPhone().isEmpty());

        // VERIFY
        verify(collaboratorRepository, times(1))
                .existsByDni(any(Dni.class));

        verify(collaboratorRepository, times(1))
                .save(any(Collaborator.class));
    }

    @Test
    void shouldCreateCollaboratorAndCreateNewOrganization() {
        // ARRANGE
        CreateCollaboratorCommand command = new CreateCollaboratorCommand(
                "00000003A", "test_name", "test_surname",
                "test@email.com", null, true, "TechCorp"
        );

        Organization newOrg = createTestOrganization();
        Collaborator expectedCollaborator = createTestCollaborator("00000003A");

        when(collaboratorRepository.existsByDni(any(Dni.class))).thenReturn(false);
        when(organizationRepository.findByName("TechCorp")).thenReturn(Optional.empty());
        when(organizationRepository.save(any(Organization.class))).thenReturn(newOrg);
        when(collaboratorRepository.save(any(Collaborator.class))).thenReturn(expectedCollaborator);

        // ACT
        Collaborator result = collaboratorService.createCollaborator(command);

        // ASSERT
        assertNotNull(result);

        // VERIFY
        verify(organizationRepository, times(1)).findByName("TechCorp");
        verify(organizationRepository, times(1)).save(any(Organization.class));
        verify(collaboratorRepository, times(1)).save(any(Collaborator.class));
    }

    @Test
    void shouldCreateCollaboratorAndReuseExistingOrganization() {
        // ARRANGE
        CreateCollaboratorCommand command = new CreateCollaboratorCommand(
                "00000003A", "test_name", "test_surname",
                "test@email.com", null, true, "TechCorp"
        );

        Organization existingOrg = createTestOrganization();
        Collaborator expectedCollaborator = createTestCollaborator("00000003A");

        when(collaboratorRepository.existsByDni(any(Dni.class))).thenReturn(false);
        when(organizationRepository.findByName("TechCorp")).thenReturn(Optional.of(existingOrg));
        when(collaboratorRepository.save(any(Collaborator.class))).thenReturn(expectedCollaborator);

        // ACT
        Collaborator result = collaboratorService.createCollaborator(command);

        // ASSERT
        assertNotNull(result);

        // VERIFY
        verify(organizationRepository, times(1)).findByName("TechCorp");
        verify(organizationRepository, never()).save(any(Organization.class));
        verify(collaboratorRepository, times(1)).save(any(Collaborator.class));
    }

    @Test
    void shouldThrowExceptionWhenDniAlreadyExists() {
        // ARRANGE
        CreateCollaboratorCommand command = createTestCommand("00000001R");

        when(collaboratorRepository.existsByDni(any(Dni.class))).thenReturn(true);

        // ACT + ASSERT
        assertThrows(CreatedCollaboratorException.class, () -> collaboratorService.createCollaborator(command));

        // VERIFY
        verify(collaboratorRepository, never())
                .save(any(Collaborator.class));
    }

    @Test
    void shouldThrowExceptionWhenDniFormatIsInvalid() {
        // ARRANGE
        CreateCollaboratorCommand command = createTestCommand("12345678A");

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> collaboratorService.createCollaborator(command));

        // VERIFY
        verify(collaboratorRepository, never())
                .existsByDni(any(Dni.class));

        verify(collaboratorRepository, never())
                .save(any(Collaborator.class));
    }

    // =====================================================
    // SEARCH COLLABORATORS TESTS
    // =====================================================

    @Test
    void shouldSearchCollaborators() {
        // ARRANGE
        PageResult<Collaborator> expectedResult = new PageResult<>(
                List.of(createTestCollaborator("00000015S")),
                0, 20, 1, 1
        );
        CollaboratorSearchCriteria criteria = new CollaboratorSearchCriteria(null, null);
        Pagination pagination = new Pagination(0, 20);

        when(collaboratorRepository.search(criteria, pagination)).thenReturn(expectedResult);

        // ACT
        PageResult<Collaborator> result = collaboratorService.searchCollaborators(criteria, pagination);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(expectedResult, result);

        // VERIFY
        verify(collaboratorRepository).search(criteria, pagination);
    }

    @Test
    void shouldSearchCollaboratorsWithFilters() {
        // ARRANGE
        PageResult<Collaborator> expectedResult = new PageResult<>(
                List.of(createTestCollaborator("00000015S")),
                0, 20, 1, 1
        );
        CollaboratorSearchCriteria criteria = new CollaboratorSearchCriteria("test_collaborator_name", null);
        Pagination pagination = new Pagination(0, 20);

        when(collaboratorRepository.search(criteria, pagination)).thenReturn(expectedResult);

        // ACT
        PageResult<Collaborator> result = collaboratorService.searchCollaborators(criteria, pagination);

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(expectedResult, result);

        // VERIFY
        verify(collaboratorRepository).search(criteria, pagination);
    }
}