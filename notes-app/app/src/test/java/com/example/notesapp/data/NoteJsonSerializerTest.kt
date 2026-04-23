package com.example.notesapp.data

import com.example.notesapp.model.Note
import org.junit.Assert.assertEquals
import org.junit.Test

class NoteJsonSerializerTest {

    private val serializer = NoteJsonSerializer()

    @Test
    fun serializeAndDeserialize_roundTripPreservesNotes() {
        val notes = listOf(
            Note(1L, "One", "First", 10L),
            Note(2L, "Two", "Second", 20L)
        )

        val restored = serializer.deserialize(serializer.serialize(notes))

        assertEquals(notes.sortedByDescending { it.updatedAt }, restored)
    }
}

