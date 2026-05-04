package com.orientation.backend.users.domain.model.entities;

import java.util.Objects;

public class Collaborator extends Person{

    private boolean external;
    private Organization organization;

    // CONSTRUCTOR (Private - use Builder)
    private Collaborator(Builder builder){
        super(builder);
        this.external = builder.external;
        this.organization = builder.organization;
    }

    // ============================================
    // FACTORY METHOD (Builder)
    // ============================================

    public static Builder builder() {
        return new Builder();
    }

    public boolean isExternal() {
        return external;
    }

    public Organization getOrganization() {
        return organization;
    }

    // ============================================
    // BUILDER (Inner Static Class)
    // ============================================
    public static class Builder extends Person.Builder<Builder>{
        private boolean external;
        private Organization organization;

        public Builder external(boolean external){
            this.external = external;
            return this;
        }

        public Builder organization(Organization organization){
            this.organization = organization;
            return this;
        }

        public Collaborator build() {
            return new Collaborator(this);
        }
    }

    // ============================================
    // EQUALS & HASHCODE (By ID - Entity)
    // ============================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Collaborator collaborator)) return false;

        if (id != null && collaborator.id != null) {
            return Objects.equals(id, collaborator.id);
        }

        return Objects.equals(dni, collaborator.dni);
    }

    @Override
    public int hashCode() {
        return (id != null) ? Objects.hash(id) : Objects.hash(dni);
    }

    @Override
    public String toString() {
        return "Collaborator: " + fullName + "\n" +
                "Dni: " + dni + "\n" +
                "Email: " + email + "\n";
    }
}
