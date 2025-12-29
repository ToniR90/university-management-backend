package com.orientation.backend.users.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullNameTest {

    // Validation Tests
   @Test
    void shouldCreateFullNameWithAllFields() {
       FullName fullName = FullName.of("Dante", "Alighiero", "Alighieri");

       assertNotNull(fullName);
       assertEquals("Dante", fullName.getName());
       assertEquals("Alighiero", fullName.getFirstSurname());
       assertEquals("Alighieri", fullName.getSecondSurname());

   }

   @Test
    void shouldCreateFullNameWithoutSecondSurname() {

   }

   // Normalize Test
   @Test
    void shouldTrimAllFields() {

   }

   // Exception Test
    @Test
    void shouldThrowExceptionForNullName() {

    }

    @Test
    void shouldThrowExceptionForEmptyName() {

    }

    @Test
    void shouldThrowExceptionForBlankName(){

    }

    @Test
    void shouldThrowExceptionForNullFirstName() {

    }

    @Test
    void shouldThrowExceptionForEmptyFirstName() {

    }

    @Test
    void shouldThrowExceptionForBlankFirstName() {

    }

    @Test
    void shouldAllowNullSecondSurname() {

    }

    @Test
    void shouldGetFullNameStringWithoutSecondSurname() {

    }

    // Equals & HashCode
    @Test
    void shouldBeEqualByValue() {

    }

    @Test
    void shouldHaveSameHashCodeForSameValue() {

    }


}