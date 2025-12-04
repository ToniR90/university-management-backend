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
public final class DniNie {

    private static final String LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";
    private static final String DNI_REGEX = "^[0-9]{8}[A-Z]$";
    private static final String NIE_REGEX = "^[XYZ][0-9]{7}[A-Z]$";

    private final String value;

    // Private constructor
    private DniNie
        this.value = validate(value);
    }

    // Factory method
    public static DniNie of(String value) {
        return new DniNie(value);
    }

    // Validation
    private static String validate(String value) {
        // TODO: Validar que no sea null

        // TODO: Normalizar (toUpperCase, trim, quitar espacios y guiones)
        String normalized = value.toUpperCase().trim().replaceAll("[\\s-]", "");

        // TODO: Validar formato (DNI o NIE)
        // if (!normalized.matches(DNI_REGEX) && !normalized.matches(NIE_REGEX)) {
        //     throw new IllegalArgumentException("Invalid DNI/NIE format: " + value);
        // }

        // TODO: Validar letra de control
        // if (!isValidControlLetter(normalized)) {
        //     throw new IllegalArgumentException("Invalid DNI/NIE control letter: " + value);
        // }

        return normalized;
    }

    // Validate control letter
    private static boolean isValidControlLetter(String dni) {
        // TODO: Extraer número (primeros 8 caracteres para DNI, convertir NIE si aplica)
        String numero = extractNumero(dni);

        // TODO: Extraer letra (último carácter)
        char letra = dni.charAt(dni.length() - 1);

        // TODO: Calcular letra esperada
        char letraEsperada = calcularLetra(numero);

        // TODO: Comparar
        return letra == letraEsperada;
    }

    // Extract number part (convert NIE if needed)
    private static String extractNumero(String dni) {
        // TODO: Si empieza con X/Y/Z (NIE), convertir a número
        // X → 0, Y → 1, Z → 2
        // Ejemplo: X1234567 → 01234567

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
    private static char calcularLetra(String numero) {
        // TODO: Convertir a int
        int num = Integer.parseInt(numero);

        // TODO: Calcular resto de dividir entre 23
        int resto = num % 23;

        // TODO: Devolver letra correspondiente
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

    // TODO: Generate equals & hashCode


    // TODO: Generate toString

}