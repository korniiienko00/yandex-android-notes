package com.korniiienko.notesapp.ui.screens.notes

import com.korniiienko.model.Note

sealed interface NotesState {
    data class Success(val notes: List<Note> = listOf()) : NotesState
    object Loading : NotesState
}