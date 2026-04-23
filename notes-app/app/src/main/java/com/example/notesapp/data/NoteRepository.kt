package com.example.notesapp.data

import com.example.notesapp.model.Note

class NoteRepository(
    private val storage: NoteStorage,
    private val serializer: NoteJsonSerializer = NoteJsonSerializer(),
    private val clock: () -> Long = { System.currentTimeMillis() }
) {

    fun getAll(): List<Note> = serializer.deserialize(storage.read())

    fun create(title: String, content: String): Note {
        val cleanedTitle = title.trim()
        val cleanedContent = content.trim()
        require(cleanedTitle.isNotEmpty()) { "Title must not be empty." }

        val now = clock()
        val note = Note(
            id = now,
            title = cleanedTitle,
            content = cleanedContent,
            updatedAt = now
        )
        val notes = (listOf(note) + getAll()).distinctBy { it.id }.sortedByDescending { it.updatedAt }
        save(notes)
        return note
    }

    fun update(id: Long, title: String, content: String): Note {
        val cleanedTitle = title.trim()
        val cleanedContent = content.trim()
        require(cleanedTitle.isNotEmpty()) { "Title must not be empty." }

        val updated = getAll().map { note ->
            if (note.id == id) {
                note.copy(
                    title = cleanedTitle,
                    content = cleanedContent,
                    updatedAt = clock()
                )
            } else {
                note
            }
        }.sortedByDescending { it.updatedAt }

        val result = updated.firstOrNull { it.id == id }
            ?: throw IllegalArgumentException("Note with id=$id not found.")
        save(updated)
        return result
    }

    fun delete(id: Long) {
        val updated = getAll().filterNot { it.id == id }
        save(updated)
    }

    private fun save(notes: List<Note>) {
        storage.write(serializer.serialize(notes))
    }
}

