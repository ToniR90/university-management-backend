package com.orientation.backend.users.domain.model.valueobjects;

import java.util.Objects;
import java.util.Optional;

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
    private final String surname;

    // Private constructor
    private FullName(String name, String surname) {
        this.name = validateRequired(name, "Nom");
        this.surname = validateRequired(surname, "Cognom");
    }

    // Factory method
    public static FullName of(String name, String surname) {
        return new FullName(name, surname);
    }

    // Validate required field
    private static String validateRequired(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " no pot ser nul");

        String trimmed = value.trim();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no pot estar buit");
        }

        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(fieldName + " supera la longitud màxima de " + MAX_LENGTH);
        }

        return trimmed;
    }


    // Getters
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    // Utility method
    public String getFullName() {
        return name + " " + surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FullName fullName)) return false;
        return Objects.equals(name, fullName.name) &&
                Objects.equals(surname, fullName.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }

    @Override
    public String toString() {
        return getFullName();
    }
}