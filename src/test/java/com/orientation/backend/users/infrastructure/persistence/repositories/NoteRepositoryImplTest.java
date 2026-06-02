package com.orientation.backend.users.infrastructure.persistence.repositories;

import com.orientation.backend.BaseIntegrationTest;
import com.orientation.backend.users.domain.model.entities.Advisor;
import com.orientation.backend.users.domain.model.entities.Note;
import com.orientation.backend.users.domain.model.entities.Student;
import com.orientation.backend.users.domain.model.enums.CurrentYear;
import com.orientation.backend.users.domain.model.enums.Degree;
import com.orientation.backend.users.domain.model.valueobjects.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({NoteRepositoryImpl.class, StudentRepositoryImpl.class, AdvisorRepositoryImpl.class})
class NoteRepositoryImplTest extends BaseIntegrationTest {

    @Autowired
    private NoteRepositoryImpl noteRepository;

    @Autowired
    private StudentRepositoryImpl studentRepository;

    @Autowired
    private AdvisorRepositoryImpl advisorRepository;

    private Student createStudent(String dni, String email) {
        return Student.builder()
                .dni(Dni.of(dni))
                .fullName(FullName.of("Test", "Student"))
                .email(Email.of(email))
                .degree(Degree.COMPUTER_ENGINEERING)
                .currentYear(CurrentYear.FIRST)
                .build();
    }

    private Advisor createAdvisor(String dni, String email) {
        return Advisor.builder()
                .dni(Dni.of(dni))
                .fullName(FullName.of("Test", "Advisor"))
                .email(Email.of(email))
                .build();
    }

    // ========== CRUD Tests ==========

    @Test
    void shouldSaveNote() {
        Note note = Note.builder()
                .content("Test note content")
                .build();

        Note saved = noteRepository.save(note);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getContent()).isEqualTo("Test note content");
    }

    @Test
    void shouldSaveNoteWithPersonId() {
        Student student = studentRepository.save(createStudent("00000026A", "student1@mail.com"));

        Note note = Note.builder()
                .personId(student.getId())
                .content("Note for student")
                .build();

        Note saved = noteRepository.save(note);

        assertThat(saved).isNotNull();
        assertThat(saved.getPersonId()).isEqualTo(student.getId());
    }

    @Test
    void shouldSaveNoteWithAdvisorId() {
        Advisor advisor = advisorRepository.save(createAdvisor("00000027G", "advisor1@mail.com"));

        Note note = Note.builder()
                .advisorId(advisor.getId())
                .content("Note by advisor")
                .build();

        Note saved = noteRepository.save(note);

        assertThat(saved).isNotNull();
        assertThat(saved.getAdvisorId()).isEqualTo(advisor.getId());
    }

    @Test
    void shouldFindNotesByPersonDni() {
        Student student = studentRepository.save(createStudent("00000028M", "student2@mail.com"));

        Note note1 = Note.builder().personId(student.getId()).content("First note").build();
        Note note2 = Note.builder().personId(student.getId()).content("Second note").build();

        noteRepository.save(note1);
        noteRepository.save(note2);

        List<Note> notes = noteRepository.findByPersonDni(Dni.of("00000028M"));

        assertThat(notes).hasSize(2);
    }

    @Test
    void shouldReturnEmptyListWhenNoNotesForDni() {
        List<Note> notes = noteRepository.findByPersonDni(Dni.of("00000029Y"));

        assertThat(notes).isEmpty();
    }

    @Test
    void shouldFindAllNotes() {
        Note note1 = Note.builder().content("First note").build();
        Note note2 = Note.builder().content("Second note").build();

        noteRepository.save(note1);
        noteRepository.save(note2);

        List<Note> all = noteRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void shouldAllowNoteWithNullPersonId() {
        Note note = Note.builder()
                .content("Orphan note")
                .build();

        Note saved = noteRepository.save(note);

        assertThat(saved).isNotNull();
        assertThat(saved.getPersonId()).isNull();
    }
}