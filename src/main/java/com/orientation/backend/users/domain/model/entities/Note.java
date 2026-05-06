package com.orientation.backend.users.domain.model.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Note {

    private UUID id;
    private UUID personId;
    private UUID advisorId;
    private String content;
    private LocalDateTime createdAt;

    // CONSTRUCTOR (Private - use Builder)
    private Note(Builder builder) {
        this.id = builder.id;
        this.personId = builder.personId;
        this.advisorId = builder.advisorId;
        this.content = Objects.requireNonNull(builder.content);
        this.createdAt = (builder.createdAt != null) ? builder.createdAt : LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getPersonId() {
        return personId;
    }

    public UUID getAdvisorId() {
        return advisorId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
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
    public static class Builder {
        private UUID id;
        private UUID personId;
        private UUID advisorId;
        private String content;
        private LocalDateTime createdAt;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder personId(UUID personId) {
            this.personId = personId;
            return this;
        }

        public Builder advisorId(UUID advisorId) {
            this.advisorId = advisorId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Note build() {
            return new Note(this);
        }
    }
}
