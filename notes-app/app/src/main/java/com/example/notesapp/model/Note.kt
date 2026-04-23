package com.example.notesapp.model

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val updatedAt: Long
)

