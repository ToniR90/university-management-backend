package com.orientation.backend.users.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FullNameTest {

    // ========== Validation Tests ==========
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
       FullName fullName = FullName.of("Dante", "Alighiero", null);

       assertNotNull(fullName);
       assertEquals("Dante", fullName.getName());
       assertEquals("Alighiero", fullName.getFirstSurname());
       assertTrue(fullName.getSecondSurname().isEmpty());
   }

   @Test
   void shouldGetFullNameString() {
       FullName fullName = FullName.of("Dante", "Alighiero", "Alighieri");

       assertEquals("Dante Alighiero Alighieri" , fullName.getFullName());
   }

   // ========== Normalize Test ==========
   @Test
    void shouldTrimAllFields() {
       FullName fullName = FullName.of("    Dante    ", "     Alighiero    ", "    Alighieri   ");

       assertNotNull(fullName);
       assertEquals("Dante", fullName.getName());
       assertEquals("Alighiero", fullName.getFirstSurname());
       assertEquals("Alighieri", fullName.getSecondSurname());
   }

   // ========== Exception Test ==========
    @Test
    void shouldThrowExceptionForNullName() {
        assertThrows(NullPointerException.class, () -> {
           FullName.of(null, "Alighiero", "Alighieri");
        });
    }

    @Test
    void shouldThrowExceptionForEmptyName() {
       assertThrows(IllegalArgumentException.class, () -> {
           FullName.of("", "Alighiero", "Alighieri");
       });
    }

    @Test
    void shouldThrowExceptionForBlankName(){
        assertThrows(IllegalArgumentException.class, () -> {
            FullName.of(" ", "Alighiero", "Alighieri");
        });
    }

    @Test
    void shouldThrowExceptionForNullFirstSurname() {
        assertThrows(NullPointerException.class, () -> {
            FullName.of("Dante", null, "Alighieri");
        });
    }

    @Test
    void shouldThrowExceptionForEmptyFirstSurname() {
        assertThrows(IllegalArgumentException.class, () -> {
            FullName.of("Dante", "", "Alighieri");
        });
    }

    @Test
    void shouldThrowExceptionForBlankFirstSurname() {
        assertThrows(IllegalArgumentException.class, () -> {
            FullName.of("Dante", " ", "Alighieri");
        });
    }

    @Test
    void shouldAllowNullSecondSurname() {
        FullName fullName = FullName.of("Dante", "Alighiero", null);

        assertNotNull(fullName);
    }

    @Test
    void shouldGetFullNameStringWithoutSecondSurname() {
        FullName fullName = FullName.of("Dante", "Alighiero", null);

        assertNotNull(fullName);
        assertEquals("Dante Alighiero", fullName.getFullName());
    }

    // ========== Equals & HashCode ==========
    @Test
    void shouldBeEqualByValue() {
        FullName fullName1 = FullName.of("Dante", "Alighiero", "Alighieri");
        FullName fullName2 = FullName.of("Dante", "Alighiero", "Alighieri");

        assertEquals(fullName1, fullName2);
    }

    @Test
    void shouldHaveSameHashCodeForSameValue() {
        FullName fullName1 = FullName.of("Dante", "Alighiero", "Alighieri");
        FullName fullName2 = FullName.of("Dante", "Alighiero", "Alighieri");

        assertEquals(fullName1.hashCode(), fullName2.hashCode());
    }


}