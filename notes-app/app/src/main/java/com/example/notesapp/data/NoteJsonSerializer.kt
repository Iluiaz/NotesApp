package com.example.notesapp.data

import com.example.notesapp.model.Note
import org.json.JSONArray
import org.json.JSONObject

class NoteJsonSerializer {

    fun serialize(notes: List<Note>): String {
        val array = JSONArray()
        notes.forEach { note ->
            array.put(
                JSONObject()
                    .put("id", note.id)
                    .put("title", note.title)
                    .put("content", note.content)
                    .put("updatedAt", note.updatedAt)
            )
        }
        return array.toString()
    }

    fun deserialize(raw: String): List<Note> {
        if (raw.isBlank()) return emptyList()
        val array = JSONArray(raw)
        return buildList {
            for (index in 0 until array.length()) {
                val item = array.getJSONObject(index)
                add(
                    Note(
                        id = item.getLong("id"),
                        title = item.getString("title"),
                        content = item.getString("content"),
                        updatedAt = item.getLong("updatedAt")
                    )
                )
            }
        }.sortedByDescending { it.updatedAt }
    }
}

