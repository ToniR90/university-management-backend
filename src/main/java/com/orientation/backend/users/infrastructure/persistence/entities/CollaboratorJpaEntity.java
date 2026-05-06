package com.orientation.backend.users.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "collaborator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "person_id")
public class CollaboratorJpaEntity extends PersonJpaEntity{

    @Column(name = "external", nullable = false)
    private boolean external;

    @Column(name = "organization_id")
    private UUID organizationId;
}
