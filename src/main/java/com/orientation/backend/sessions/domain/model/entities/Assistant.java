package com.orientation.backend.sessions.domain.model.entities;

import java.util.UUID;

public class Assistant {
    private UUID sessionId;
    private UUID personId;
    private boolean registered;
    private boolean attended;

    private Assistant(Builder builder) {
        this.sessionId = builder.sessionId;
        this.personId = builder.personId;
        this.registered = builder.registered;
        this.attended = builder.attended;
    }

    // ============================================
    // FACTORY METHOD (Builder)
    // ============================================
    public static Builder builder() {
        return new Builder();
    }


    // ============================================
    // GETTERS
    // ============================================

    public UUID getSessionId() {
        return sessionId;
    }

    public UUID getPersonId() {
        return personId;
    }

    public boolean isRegistered() {
        return registered;
    }

    public boolean isAttended() {
        return attended;
    }


    // ============================================
    // BUILDER (Inner Static Class)
    // ============================================

    public static class Builder {
        private UUID sessionId;
        private UUID personId;
        private boolean registered;
        private boolean attended;

        private Builder() {

        }

        public Builder sessionId(UUID sessionId){
            this.sessionId = sessionId;
            return this;
        }

        public Builder personId(UUID personId){
            this.personId = personId;
            return this;
        }

        public Builder registered(boolean registered) {
            this.registered = registered;
            return this;
        }

        public Builder attended(boolean attended) {
            this.attended = attended;
            return this;
        }

        /**
         * Builds the Assistant instance.
         * Validates all required fields are present.
         *
         * @return new Assitant instance
         * @throws NullPointerException if required fields are null
         * @throws IllegalArgumentException if validation fails
         */
        public Assistant build() {
            return new Assistant(this);
        }
    }
}
