package com.orientation.backend.sessions.domain.model.entities;

import java.util.UUID;

public class AdvisorInSession {
    private UUID sessionId;
    private UUID advisorId;

    private AdvisorInSession(Builder builder){
        this.sessionId = builder.sessionId;
        this.advisorId = builder.advisorId;
    }

    // ============================================
    // FACTORY METHOD (Builder)
    // ============================================
    public static Builder builder(){
        return new Builder();
    }

    // ============================================
    // GETTERS
    // ============================================
    public UUID getSessionId() {
        return sessionId;
    }
    public UUID getAdvisorId() {
        return advisorId;
    }

    // ============================================
    // BUILDER (Inner Static Class)
    // ============================================
    public static class Builder{
        private UUID sessionId;
        private UUID advisorId;

        private Builder(){}

        public Builder sessionId(UUID sessionId){
            this.sessionId = sessionId;
            return this;
        }

        public Builder advisorId(UUID advisorId){
            this.advisorId = advisorId;
            return this;
        }

        public AdvisorInSession build(){
            return new AdvisorInSession(this);
        }
    }
}