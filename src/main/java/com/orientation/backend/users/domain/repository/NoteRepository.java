package com.orientation.backend.users.domain.repository;

import com.orientation.backend.users.domain.model.entities.Note;
import com.orientation.backend.users.domain.model.valueobjects.Dni;

import java.util.List;
import java.util.Optional;

public interface NoteRepository {
    Note save(Note note);
    List<Note> findByPersonDni(Dni dni);
    List<Note> findAll();
}
