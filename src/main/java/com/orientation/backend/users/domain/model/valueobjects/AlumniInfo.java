package com.orientation.backend.users.domain.model.valueobjects;

import com.orientation.backend.users.domain.model.enums.AlumniType;
import java.time.Year;
import java.util.Objects;
import java.util.Optional;

/**
 * Value Object representing alumni information.
 * Composite VO with consistency validation.
 *
 * Business rules:
 * - If isAlumni = false → type and graduationYear must be null
 * - If isAlumni = true → type and graduationYear must be present
 * - graduationYear must be between 1900 and current year
 *
 * Immutable and self-validated.
 */
public final class AlumniInfo {

    private static final int MIN_GRADUATION_YEAR = 1900;

    private final boolean isAlumni;
    private final AlumniType type; // Nullable
    private final Integer graduationYear; // Nullable

    // Private constructor
    private AlumniInfo(boolean isAlumni, AlumniType type, Integer graduationYear) {
        validateConsistency(isAlumni, type, graduationYear);
        this.isAlumni = isAlumni;
        this.type = type;
        this.graduationYear = graduationYear;
    }

    // Factory method: not alumni
    public static AlumniInfo notAlumni() {
        return new AlumniInfo(false, null, null);
    }

    // Factory method: alumni
    public static AlumniInfo createAlumni(AlumniType type, Integer graduationYear) {
        return new AlumniInfo(true, type, graduationYear);
    }

    // Consistency validation
    private static void validateConsistency(boolean isAlumni, AlumniType type, Integer graduationYear) {
        // TODO: Validar consistencia

        if (!isAlumni) {
            // Si no es alumni, type y year deben ser null
            // if (type != null || graduationYear != null) {
            //     throw new IllegalArgumentException("Non-alumni cannot have type or graduation year");
            // }
        } else {
            // Si es alumni, type y year deben estar presentes
            // TODO: Validar que type no sea null

            // TODO: Validar que graduationYear no sea null

            // TODO: Validar año (>= 1900, <= año actual)
            // int currentYear = Year.now().getValue();
            // if (graduationYear < MIN_GRADUATION_YEAR || graduationYear > currentYear) {
            //     throw new IllegalArgumentException("Invalid graduation year: " + graduationYear);
            // }
        }
    }

    // Getters
    public boolean isAlumni() {
        return isAlumni;
    }

    public Optional<AlumniType> getType() {
        return Optional.ofNullable(type);
    }

    public Optional<Integer> getGraduationYear() {
        return Optional.ofNullable(graduationYear);
    }

    // TODO: Generate equals & hashCode (Select ALL three fields)


    // TODO: Generate toString

}