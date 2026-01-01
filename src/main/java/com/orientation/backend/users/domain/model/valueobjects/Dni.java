package com.orientation.backend.users.domain.model.valueobjects;

import java.util.Objects;

/**
 * Value Object representing Spanish DNI or NIE.
 * Validates format and control letter using official algorithm.
 *
 * DNI format: 8 digits + letter (e.g., 12345678Z)
 * NIE format: X/Y/Z + 7 digits + letter (e.g., X1234567L)
 *
 * Immutable and self-validated.
 */

public final class Dni {

    private static final String LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";
    private static final String DNI_REGEX = "^[0-9]{8}[A-Z]$";
    private static final String NIE_REGEX = "^[XYZ][0-9]{7}[A-Z]$";

    private final String value;

    // Private constructor
    private Dni(String value) {
        this.value = validate(value);
    }

    // Factory method
    public static Dni of(String value) {
        return new Dni(value);
    }

    // Validation
    private static String validate(String value) {
        Objects.requireNonNull(value, "El DNI/NIE no pot ser nul");

        String normalized = value.toUpperCase().trim().replaceAll("[\\s-]", "");

        if (!normalized.matches(DNI_REGEX) && !normalized.matches(NIE_REGEX)) {
            throw new IllegalArgumentException("El format del DNI/NIE no és vàlid: " + value);
        }

        if (!isValidControlLetter(normalized)) {
            throw new IllegalArgumentException("La lletra de control del DNI/NIE no és vàlida: " + value);
        }

        return normalized;
    }

    // Validate control letter
    private static boolean isValidControlLetter(String dni) {
        String numero = extractNumber(dni);
        char letra = dni.charAt(dni.length() - 1);
        char letraEsperada = calculateLetter(numero);
        return letra == letraEsperada;
    }

    // Extract number part (convert NIE if needed)
    private static String extractNumber(String dni) {
        // X → 0, Y → 1, Z → 2
        if (dni.startsWith("X")) {
            return "0" + dni.substring(1, 8);
        } else if (dni.startsWith("Y")) {
            return "1" + dni.substring(1, 8);
        } else if (dni.startsWith("Z")) {
            return "2" + dni.substring(1, 8);
        } else {
            return dni.substring(0, 8);
        }
    }

    // Calculate control letter
    private static char calculateLetter(String numero) {
        int num = Integer.parseInt(numero);
        int resto = num % 23;
        return LETRAS_DNI.charAt(resto);
    }

    // Getter
    public String getValue() {
        return value;
    }

    // Utility methods
    public boolean isDni() {
        return value.matches(DNI_REGEX);
    }

    public boolean isNie() {
        return value.matches(NIE_REGEX);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dni dni)) return false;
        return Objects.equals(value, dni.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}