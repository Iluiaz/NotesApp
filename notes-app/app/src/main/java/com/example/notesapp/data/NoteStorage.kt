package com.example.notesapp.data

interface NoteStorage {
    fun read(): String
    fun write(serialized: String)
}

