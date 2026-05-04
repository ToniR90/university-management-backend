package com.orientation.backend.users.domain.model.entities;

import java.util.Objects;

public class Advisor extends Person{

    // CONSTRUCTOR (Private - use Builder)
    private Advisor(Builder builder){
        super(builder);
    }

    // ============================================
    // FACTORY METHOD (Builder)
    // ============================================

    public static Builder builder() {
        return new Builder();
    }

    // ============================================
    // EQUALS & HASHCODE (By ID - Entity)
    // ============================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Advisor advisor)) return false;

        if (id != null && advisor.id != null) {
            return Objects.equals(id, advisor.id);
        }

        return Objects.equals(dni, advisor.dni);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : Objects.hash(dni);
    }

    @Override
    public String toString() {
        return "Advisor: " + fullName + "\n" +
                "Dni: " + dni + "\n" +
                "Email: " + email + "\n";
    }

    // ============================================
    // BUILDER (Inner Static Class)
    // ============================================
    public static class Builder extends Person.Builder<Builder> {
        public Advisor build() {
            return new Advisor(this);
        }
    }
}
