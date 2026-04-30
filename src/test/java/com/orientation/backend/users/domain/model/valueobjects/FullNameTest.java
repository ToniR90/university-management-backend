package com.orientation.backend.users.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullNameTest {

    // ========== Validation Tests ==========
   @Test
    void shouldCreateFullNameWithAllFields() {
       FullName fullName = FullName.of("Dante", "Alighiero");

       assertNotNull(fullName);
       assertEquals("Dante", fullName.getName());
       assertEquals("Alighiero", fullName.getSurname());
   }

   @Test
   void shouldGetFullNameString() {
       FullName fullName = FullName.of("Dante", "Alighiero");

       assertEquals("Dante Alighiero" , fullName.getFullName());
   }

   // ========== Normalize Test ==========
   @Test
    void shouldTrimAllFields() {
       FullName fullName = FullName.of("    Dante    ", "     Alighiero    ");

       assertNotNull(fullName);
       assertEquals("Dante", fullName.getName());
       assertEquals("Alighiero", fullName.getSurname());
   }

   // ========== Exception Test ==========
    @Test
    void shouldThrowExceptionForNullName() {
        assertThrows(NullPointerException.class, () -> {
           FullName.of(null, "Alighiero");
        });
    }

    @Test
    void shouldThrowExceptionForEmptyName() {
       assertThrows(IllegalArgumentException.class, () -> {
           FullName.of("", "Alighiero");
       });
    }

    @Test
    void shouldThrowExceptionForBlankName(){
        assertThrows(IllegalArgumentException.class, () -> {
            FullName.of(" ", "Alighiero");
        });
    }

    @Test
    void shouldThrowExceptionForNullSurname() {
        assertThrows(NullPointerException.class, () -> {
            FullName.of("Dante", null);
        });
    }

    @Test
    void shouldThrowExceptionForEmptySurname() {
        assertThrows(IllegalArgumentException.class, () -> {
            FullName.of("Dante", "");
        });
    }

    @Test
    void shouldThrowExceptionForBlankSurname() {
        assertThrows(IllegalArgumentException.class, () -> {
            FullName.of("Dante", " ");
        });
    }

    // ========== Equals & HashCode ==========
    @Test
    void shouldBeEqualByValue() {
        FullName fullName1 = FullName.of("Dante", "Alighiero");
        FullName fullName2 = FullName.of("Dante", "Alighiero");

        assertEquals(fullName1, fullName2);
    }

    @Test
    void shouldHaveSameHashCodeForSameValue() {
        FullName fullName1 = FullName.of("Dante", "Alighiero");
        FullName fullName2 = FullName.of("Dante", "Alighiero");

        assertEquals(fullName1.hashCode(), fullName2.hashCode());
    }


}