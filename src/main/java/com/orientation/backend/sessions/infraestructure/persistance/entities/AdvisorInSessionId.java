package com.orientation.backend.sessions.infraestructure.persistance.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class AdvisorInSessionId implements Serializable {
    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "advisor_id")
    private UUID advisorId;

    public AdvisorInSessionId() {
    }

    public AdvisorInSessionId(UUID sessionId, UUID advisorId) {
        this.sessionId = sessionId;
        this.advisorId = advisorId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public UUID getAdvisorId() {
        return advisorId;
    }

    @Override
    public boolean equals(Object o){
        if (o == null || getClass() != o.getClass())
            return false;
        AdvisorInSessionId that = (AdvisorInSessionId) o;
        return Objects.equals(sessionId, that.sessionId) && Objects.equals(advisorId, that.advisorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, advisorId);
    }
}
