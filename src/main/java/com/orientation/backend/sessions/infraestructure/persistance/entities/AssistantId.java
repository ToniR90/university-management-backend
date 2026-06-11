package com.orientation.backend.sessions.infraestructure.persistance.entities;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class AssistantId implements Serializable {
    private UUID sessionId;
    private UUID personId;

    public AssistantId() {
    }

    public AssistantId(UUID sessionId, UUID personId) {
        this.sessionId = sessionId;
        this.personId = personId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public UUID getPersonId() {
        return personId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        AssistantId that = (AssistantId) o;
        return Objects.equals(sessionId, that.sessionId) && Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, personId);
    }
}
