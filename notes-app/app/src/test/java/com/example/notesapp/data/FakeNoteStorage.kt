package com.example.notesapp.data

class FakeNoteStorage(
    initialValue: String = "[]"
) : NoteStorage {

    private var value: String = initialValue

    override fun read(): String = value

    override fun write(serialized: String) {
        value = serialized
    }
}

