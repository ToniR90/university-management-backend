package com.orientation.backend.users.domain.model.entities;

import java.util.UUID;

public class Organization {

    private UUID id;
    private String name;

    // CONSTRUCTOR (Private - use Builder)
    private Organization(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // ============================================
    // FACTORY METHOD (Builder)
    // ============================================
    public static Builder builder(){
        return new Builder();
    }

    // ============================================
    // BUILDER (Inner Static Class)
    // ============================================
    public static class Builder{
        private UUID id;
        private String name;

        public Builder id(UUID id){
            this.id = id;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Organization build() {
            return new Organization(this);
        }
    }
}
