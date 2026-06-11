package com.orientation.backend.sessions.infraestructure.persistance.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "assistants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssistantJpaEntity {

    @EmbeddedId
    private AssistantId id;

    @Column(name = "registered")
    private boolean registered;

    @Column(name = "attended")
    private boolean attended;
}
