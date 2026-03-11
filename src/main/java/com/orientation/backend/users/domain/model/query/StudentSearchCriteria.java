package com.orientation.backend.users.domain.model.query;

import com.orientation.backend.users.domain.model.enums.CurrentYear;

/**
 * This class encapsule all the parameters the client can send to make the filters
 * All the parameters can be null, as the filters are optional
 */
public class StudentSearchCriteria {

    private final String name;
    private final String dni;
    private final CurrentYear currentYear;
    private final Boolean isAlumni;


    public StudentSearchCriteria(String name, String dni, CurrentYear currentYear, Boolean isAlumni) {
        this.name = name;
        this.dni = dni;
        this.currentYear = currentYear;
        this.isAlumni = isAlumni;
    }

    public String getName() {
        return name;
    }

    public String getDni() {
        return dni;
    }

    public CurrentYear getCurrentYear() {
        return currentYear;
    }

    public Boolean getIsAlumni() {
        return isAlumni;
    }
}
