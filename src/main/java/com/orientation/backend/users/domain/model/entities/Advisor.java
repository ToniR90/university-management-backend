package com.orientation.backend.users.domain.model.entities;

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
    // BUILDER (Inner Static Class)
    // ============================================
    public static class Builder extends Person.Builder<Builder> {
        public Advisor build() {
            return new Advisor(this);
        }
    }
}
