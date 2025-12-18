package com.orientation.backend.users.domain.model.valueobjects;

import java.util.Objects;

/**
 * Value Object representing a phone number in E.164 format.
 * Format: +[country code][number] (e.g., +34612345678)
 *
 * Normalizes input by removing spaces, parentheses, and hyphens.
 * Adds +34 prefix if not present (Spanish default).
 *
 * Immutable and self-validated.
 */
public final class Phone {

    private static final String DEFAULT_COUNTRY_CODE = "+34";
    private static final String PHONE_REGEX = "^\\+[1-9][0-9]{7,14}$"; // E.164 format

    private final String value;

    // Private constructor
    private Phone(String value) {
        this.value = validate(value);
    }

    // Factory method
    // Fail-fast (throw exception if its null or invalid)
    public static Phone of(String value) {
        return new Phone(value);
    }

    // Nullable
    public static Phone ofNullable(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return new Phone(value);
    }

    // Validation
    private static String validate(String value) {
        Objects.requireNonNull(value, "El telèfon no pot ser nul");

        // 1. Normalize
        String normalized = normalize(value);

        // 2. Validate E.164 format
        if (!normalized.matches(PHONE_REGEX)) {
            throw new IllegalArgumentException("El format del telèfon no és vàlid. S'espera format E.164: " + value);
        }

        return normalized;
    }

    private static String normalize(String phone) {
        // 1. Trim
        String cleaned = phone.trim();

        // 2. Delete empty spaces, "-"
        cleaned = cleaned.replaceAll("[\\s()\\-]", "");

        // 3. Convert 00 a +
        if (cleaned.startsWith("00")) {
            cleaned = "+" + cleaned.substring(2);
        }

        // 4. If there's no prefixes, default is Spain
        if (!cleaned.startsWith("+") && cleaned.matches("^[6789]\\d{8}$")) {
            cleaned = DEFAULT_COUNTRY_CODE + cleaned;
        }

        // 5. If there's no "+", but there's numbers
        if (!cleaned.startsWith("+") && cleaned.matches("^\\d+$")) {
            cleaned = DEFAULT_COUNTRY_CODE + cleaned;
        }

        return cleaned;
    }

    // Getter
    public String getValue() {
        return value;
    }

    // Utility method
    public String getCountryCode() {
        // Extract country code (everything after + until first non-digit)
        int i = 1; // Start after +
        while (i < value.length() && Character.isDigit(value.charAt(i))) {
            i++;
        }
        return value.substring(0, i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Phone phone)) return false;
        return value.equals(phone.value);
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