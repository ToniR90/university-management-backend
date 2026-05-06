package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.users.domain.model.entities.Note;
import com.orientation.backend.users.domain.model.valueobjects.Dni;
import com.orientation.backend.users.domain.repository.NoteRepository;
import com.orientation.backend.users.infrastructure.persistence.entities.NoteJpaEntity;
import com.orientation.backend.users.infrastructure.persistence.mappers.NoteJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoteRepositoryImpl implements NoteRepository {

    private final SpringDataNoteRepository jpaRepository;

    @Override
    public Note save(Note note) {
        NoteJpaEntity jpaEntity = NoteJpaMapper.toJpaEntity(note);

        NoteJpaEntity saved = jpaRepository.save(jpaEntity);

        return NoteJpaMapper.toDomain(saved);
    }

    @Override
    public List<Note> findByPersonDni(Dni dni) {
        return jpaRepository.findByPersonDni(dni.getValue()).stream().map(NoteJpaMapper::toDomain).toList();
    }

    @Override
    public List<Note> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(NoteJpaMapper::toDomain)
                .toList();
    }
}
