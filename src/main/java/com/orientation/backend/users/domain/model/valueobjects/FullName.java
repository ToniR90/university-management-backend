package com.orientation.backend.users.domain.model.valueobjects;

import java.util.Objects;

/**
 * Value Object representing a person's full name.
 * Composite VO with three parts: name, first surname, second surname.
 *
 * - name: required
 * - firstSurname: required
 * - secondSurname: optional (can be null for international students)
 *
 * Immutable and self-validated.
 */
public final class FullName {

    private static final int MAX_LENGTH = 100;

    private final String name;
    private final String firstSurname;
    private final String secondSurname; // Nullable

    // Private constructor
    private FullName(String name, String firstSurname, String secondSurname) {
        this.name = validateRequired(name, "Name");
        this.firstSurname = validateRequired(firstSurname, "First surname");
        this.secondSurname = validateOptional(secondSurname, "Second surname");
    }

    // Factory method
    public static FullName of(String name, String firstSurname, String secondSurname) {
        return new FullName(name, firstSurname, secondSurname);
    }

    // Validate required field
    private static String validateRequired(String value, String fieldName) {
        // TODO: Validar que no sea null

        // TODO: Trim
        String trimmed = value.trim();

        // TODO: Validar que no esté vacío
        // if (trimmed.isEmpty()) {
        //     throw new IllegalArgumentException(fieldName + " cannot be empty");
        // }

        // TODO: Validar longitud máxima
        // if (trimmed.length() > MAX_LENGTH) {
        //     throw new IllegalArgumentException(fieldName + " exceeds maximum length of " + MAX_LENGTH);
        // }

        return trimmed;
    }

    // Validate optional field
    private static String validateOptional(String value, String fieldName) {
        // TODO: Si es null, devolver null
        if (value == null) {
            return null;
        }

        // TODO: Si no es null, validar igual que required
        return validateRequired(value, fieldName);
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    // Utility method
    public String getFullName() {
        // TODO: Construir nombre completo
        // Si secondSurname es null: "Name FirstSurname"
        // Si no: "Name FirstSurname SecondSurname"

        if (secondSurname == null) {
            return name + " " + firstSurname;
        }
        return name + " " + firstSurname + " " + secondSurname;
    }

    // TODO: Generate equals & hashCode (Select ALL three fields: name, firstSurname, secondSurname)


    // TODO: Generate toString

}