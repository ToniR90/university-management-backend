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
        if (!isAlumni) {
            // Non-alumni: type and year must be null
            if (type != null || graduationYear != null) {
                throw new IllegalArgumentException("No es pot determinar un any de graduació ni tipus d'alumni si no ho és");
            }
        } else {
            // Alumni: type and year must be present
            if (type == null || graduationYear == null) {
                throw new IllegalArgumentException("Falten per determinar el tipus d'alumni o l'any de graduació");
            }

            // Validate year range
            int currentYear = Year.now().getValue();
            if (graduationYear < MIN_GRADUATION_YEAR || graduationYear > currentYear) {
                throw new IllegalArgumentException("L'any de graduació no és correcte: " + graduationYear);
            }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlumniInfo that)) return false;
        return isAlumni == that.isAlumni &&
                Objects.equals(type, that.type) &&
                Objects.equals(graduationYear, that.graduationYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAlumni, type, graduationYear);
    }

    @Override
    public String toString() {
        if (!isAlumni) {
            return "L'usuari no és alumni";
        }
        return "Alumni info: " + "\n" +
                "Tipus: " + type + "\n" +
                "Any de graduació: " + graduationYear;
    }
}