package com.example.notesapp.data

import android.content.SharedPreferences

class SharedPreferencesNoteStorage(
    private val preferences: SharedPreferences
) : NoteStorage {

    override fun read(): String = preferences.getString(KEY_NOTES, "[]") ?: "[]"

    override fun write(serialized: String) {
        preferences.edit().putString(KEY_NOTES, serialized).apply()
    }

    companion object {
        private const val KEY_NOTES = "notes_json"
    }
}

