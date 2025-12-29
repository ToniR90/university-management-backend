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
    private final String firstSurname;
    private final String secondSurname; // Nullable

    // Private constructor
    private FullName(String name, String firstSurname, String secondSurname) {
        this.name = validateRequired(name, "Nom");
        this.firstSurname = validateRequired(firstSurname, "Primer cognom");
        this.secondSurname = validateOptional(secondSurname, "Segon cognom");
    }

    // Factory method
    public static FullName of(String name, String firstSurname, String secondSurname) {
        return new FullName(name, firstSurname, secondSurname);
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

    // Validate optional field
    private static String validateOptional(String value, String fieldName) {
        if (value == null) {
            return null;
        }
        return validateRequired(value, fieldName);
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public Optional<String> getSecondSurname() {
        return Optional.ofNullable(secondSurname);
    }

    // Utility method
    public String getFullName() {
        if (secondSurname == null) {
            return name + " " + firstSurname;
        }
        return name + " " + firstSurname + " " + secondSurname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FullName fullName)) return false;
        return Objects.equals(name, fullName.name) &&
                Objects.equals(firstSurname, fullName.firstSurname) &&
                Objects.equals(secondSurname, fullName.secondSurname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, firstSurname, secondSurname);
    }

    @Override
    public String toString() {
        return getFullName();
    }
}