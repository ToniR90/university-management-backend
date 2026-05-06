package com.orientation.backend.users.infrastructure.persistence.mappers;

import com.orientation.backend.users.domain.model.entities.Note;
import com.orientation.backend.users.infrastructure.persistence.entities.NoteJpaEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class NoteJpaMapper {

    // ========== JPA -> Domain ==========
    public static Note toDomain(NoteJpaEntity jpaEntity) {
        Note.Builder builder = Note.builder();

        return builder
                .id(jpaEntity.getId())
                .personId(jpaEntity.getPersonId())
                .advisorId(jpaEntity.getAdvisorId())
                .content(jpaEntity.getContent())
                .createdAt(jpaEntity.getCreatedAt())
                .build();
    }

    // ========== Domain -> Jpa ==========
    public static NoteJpaEntity toJpaEntity(Note note) {
        NoteJpaEntity jpaEntity = new NoteJpaEntity();

        jpaEntity.setId(note.getId());
        jpaEntity.setPersonId(note.getPersonId());
        jpaEntity.setAdvisorId(note.getAdvisorId());
        jpaEntity.setContent(note.getContent());
        jpaEntity.setCreatedAt(note.getCreatedAt());

        return jpaEntity;
    }
}
