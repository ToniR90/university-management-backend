package com.orientation.backend.users.domain.model.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationTest {

    // ========== Builder Tests ==========

    @Test
    void shouldCreateOrganizationWithName() {
        Organization organization = Organization.builder()
                .name("Test Organization")
                .build();

        assertNotNull(organization);
        assertEquals("Test Organization", organization.getName());
    }

    @Test
    void shouldCreateOrganizationWithIdAndName() {
        UUID id = UUID.randomUUID();
        Organization organization = Organization.builder()
                .id(id)
                .name("Test Organization")
                .build();

        assertNotNull(organization);
        assertEquals(id, organization.getId());
        assertEquals("Test Organization", organization.getName());
    }

    @Test
    void shouldAllowNullId() {
        Organization organization = Organization.builder()
                .name("Test Organization")
                .build();

        assertNull(organization.getId());
    }

    @Test
    void shouldAllowNullName() {
        Organization organization = Organization.builder()
                .name(null)
                .build();

        assertNull(organization.getName());
    }

    @Test
    void shouldCreateTwoOrganizationsWithDifferentIds() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Organization org1 = Organization.builder().id(id1).name("Org 1").build();
        Organization org2 = Organization.builder().id(id2).name("Org 2").build();

        assertNotEquals(org1.getId(), org2.getId());
        assertNotEquals(org1.getName(), org2.getName());
    }
}