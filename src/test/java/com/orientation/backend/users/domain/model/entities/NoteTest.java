package com.orientation.backend.users.domain.model.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    // ========== Helper Methods ==========

    private Note createNote() {
        return Note.builder()
                .content("Test note content")
                .build();
    }

    private Note createFullNote() {
        return Note.builder()
                .id(UUID.randomUUID())
                .personId(UUID.randomUUID())
                .advisorId(UUID.randomUUID())
                .content("Test note content")
                .createdAt(LocalDateTime.now())
                .build();
    }

    // ========== Builder Tests ==========

    @Test
    void shouldCreateNoteWithContent() {
        Note note = createNote();

        assertNotNull(note);
        assertEquals("Test note content", note.getContent());
    }

    @Test
    void shouldCreateFullNote() {
        Note note = createFullNote();

        assertNotNull(note);
        assertNotNull(note.getId());
        assertNotNull(note.getPersonId());
        assertNotNull(note.getAdvisorId());
        assertNotNull(note.getContent());
        assertNotNull(note.getCreatedAt());
    }

    @Test
    void shouldThrowExceptionForNullContent() {
        assertThrows(NullPointerException.class, () ->
                Note.builder()
                        .content(null)
                        .build()
        );
    }

    @Test
    void shouldAllowNullPersonId() {
        Note note = Note.builder()
                .content("Test content")
                .personId(null)
                .build();

        assertNull(note.getPersonId());
    }

    @Test
    void shouldAllowNullAdvisorId() {
        Note note = Note.builder()
                .content("Test content")
                .advisorId(null)
                .build();

        assertNull(note.getAdvisorId());
    }

    @Test
    void shouldSetCreatedAtAutomaticallyWhenNotProvided() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        Note note = createNote();
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertNotNull(note.getCreatedAt());
        assertTrue(note.getCreatedAt().isAfter(before));
        assertTrue(note.getCreatedAt().isBefore(after));
    }

    @Test
    void shouldUseProvidedCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 12, 0);
        Note note = Note.builder()
                .content("Test content")
                .createdAt(createdAt)
                .build();

        assertEquals(createdAt, note.getCreatedAt());
    }
}