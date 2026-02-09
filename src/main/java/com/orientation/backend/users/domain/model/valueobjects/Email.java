package com.orientation.backend.users.domain.model.valueobjects;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing an email address.
 * Immutable and self-validated.
 */
public final class Email {


    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final String value;

    private Email(String value){
        this.value = validate(value);
    }

    //Factory method
    public static Email of(String value) {
        return new Email(value);  // Fail-fast si null
    }

    public static Email ofNullable(String value) {
        return value == null ? null : new Email(value);
    }

    //Validation
    private static String validate(String value){
        Objects.requireNonNull(value, "El correu electrònic no pot ser nul");

        String normalized = value.trim().toLowerCase();

        if(normalized.isEmpty()){
            throw new IllegalArgumentException("El correu electrònic no pot estar en blanc");
        }

        if(!EMAIL_REGEX.matcher(normalized).matches()){
            throw new IllegalArgumentException("El format del correu electrònic no és vàlid");
        }

        return normalized;
    }

    public String getValue(){
        return value;
    }

    public String getDomain(){
        return value.substring(value.indexOf("@") + 1);
    }

    public boolean isFromDomain(String domain) {
        return getDomain().equalsIgnoreCase(domain);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Email email)) return false;
        return value.equals(email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString(){
        return value;
    }
}
