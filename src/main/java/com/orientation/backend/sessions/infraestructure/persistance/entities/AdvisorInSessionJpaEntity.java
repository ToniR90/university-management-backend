package com.orientation.backend.sessions.infraestructure.persistance.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "advisors_in_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvisorInSessionJpaEntity {

    @EmbeddedId
    private AdvisorInSessionId id;
}