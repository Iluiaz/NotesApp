package com.example.notesapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.notesapp.data.NoteRepository
import com.example.notesapp.data.SharedPreferencesNoteStorage
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.databinding.DialogEditNoteBinding
import com.example.notesapp.model.Note
import com.example.notesapp.ui.NotesAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NotesAdapter
    private lateinit var repository: NoteRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = NoteRepository(
            storage = SharedPreferencesNoteStorage(
                preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            )
        )

        adapter = NotesAdapter(
            onEdit = { note -> showNoteDialog(note) },
            onDelete = { note -> confirmDelete(note) }
        )

        binding.notesRecyclerView.adapter = adapter
        binding.addNoteFab.setOnClickListener { showNoteDialog() }

        renderNotes()
    }

    private fun renderNotes() {
        val notes = repository.getAll()
        adapter.submitList(notes)
        binding.emptyStateTextView.isVisible = notes.isEmpty()
    }

    private fun showNoteDialog(note: Note? = null) {
        val dialogBinding = DialogEditNoteBinding.inflate(layoutInflater)
        dialogBinding.titleEditText.setText(note?.title.orEmpty())
        dialogBinding.contentEditText.setText(note?.content.orEmpty())

        val dialogTitle = if (note == null) R.string.add_note else R.string.edit_note
        AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.save, null)
            .setNegativeButton(R.string.cancel, null)
            .create()
            .also { dialog ->
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val title = dialogBinding.titleEditText.text?.toString().orEmpty()
                        val content = dialogBinding.contentEditText.text?.toString().orEmpty()
                        runCatching {
                            if (note == null) {
                                repository.create(title, content)
                            } else {
                                repository.update(note.id, title, content)
                            }
                        }.onSuccess {
                            renderNotes()
                            dialog.dismiss()
                        }.onFailure {
                            Toast.makeText(this, it.message ?: getString(R.string.validation_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                dialog.show()
            }
    }

    private fun confirmDelete(note: Note) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_note)
            .setMessage(getString(R.string.delete_confirmation, note.title))
            .setPositiveButton(R.string.delete) { _, _ ->
                repository.delete(note.id)
                renderNotes()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    companion object {
        private const val PREFS_NAME = "notes_prefs"
    }
}

