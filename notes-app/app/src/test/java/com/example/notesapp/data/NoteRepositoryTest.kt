package com.example.notesapp.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NoteRepositoryTest {

    private lateinit var storage: FakeNoteStorage
    private lateinit var repository: NoteRepository
    private var time = 1000L

    @Before
    fun setUp() {
        storage = FakeNoteStorage()
        repository = NoteRepository(
            storage = storage,
            clock = { ++time }
        )
    }

    @Test
    fun create_savesNoteAndReturnsIt() {
        val note = repository.create("  Test title ", " Some text ")

        assertEquals("Test title", note.title)
        assertEquals("Some text", note.content)
        assertEquals(1, repository.getAll().size)
    }

    @Test
    fun create_emptyTitleThrows() {
        assertThrows(IllegalArgumentException::class.java) {
            repository.create("   ", "Body")
        }
    }

    @Test
    fun update_changesExistingNote() {
        val note = repository.create("Original", "Body")

        val updated = repository.update(note.id, "Updated", "New body")

        assertEquals("Updated", updated.title)
        assertEquals("New body", updated.content)
        assertEquals("Updated", repository.getAll().first().title)
    }

    @Test
    fun delete_removesNoteFromStorage() {
        val note = repository.create("Delete me", "Body")

        repository.delete(note.id)

        assertTrue(repository.getAll().isEmpty())
    }

    @Test
    fun getAll_loadsSavedNotes() {
        val serializer = NoteJsonSerializer()
        storage.write(
            serializer.serialize(
                listOf(
                    com.example.notesapp.model.Note(
                        id = 1L,
                        title = "Saved note",
                        content = "Persisted body",
                        updatedAt = 5000L
                    )
                )
            )
        )

        val notes = repository.getAll()

        assertEquals(1, notes.size)
        assertEquals("Saved note", notes.first().title)
    }
}

