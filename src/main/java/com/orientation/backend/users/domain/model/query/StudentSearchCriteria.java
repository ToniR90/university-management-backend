package com.orientation.backend.users.domain.model.query;

import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.Degree;

/**
 * This class encapsule all the parameters the client can send to make the filters
 * All the parameters can be null, as the filters are optional
 */
public class StudentSearchCriteria {

    private final String name;
    private final String dni;
    private final CurrentYear currentYear;
    private final Boolean isAlumni;
    private final Degree degree;


    public StudentSearchCriteria(String name, String dni, CurrentYear currentYear, Boolean isAlumni, Degree degree) {
        this.name = name;
        this.dni = dni;
        this.currentYear = currentYear;
        this.isAlumni = isAlumni;
        this.degree = degree;
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

    public Degree getDegree() {
        return degree;
    }
}
