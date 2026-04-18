package com.orientation.backend.users.infrastructure.persistence.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "person_id")
public class StudentJpaEntity extends PersonJpaEntity{

    @Column(name = "degree", nullable = false)
    private String degree;

    @Column(name = "current_year", nullable = false)
    private String currentYear;

    @Column(name = "is_alumni", nullable = false)
    private Boolean isAlumni;

    @Column(name = "alumni_type")
    private String alumniType;

    @Column(name = "graduation_year")
    private Integer graduationYear;

    @Column(name = "counselor_notes", columnDefinition = "TEXT")
    private String counselorNotes;
}
