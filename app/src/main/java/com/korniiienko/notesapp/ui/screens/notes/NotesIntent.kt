package com.korniiienko.notesapp.ui.screens.notes

sealed interface NotesIntent {
    object LoadNotes : NotesIntent
    data class DeleteNote(val noteId: String) : NotesIntent
}