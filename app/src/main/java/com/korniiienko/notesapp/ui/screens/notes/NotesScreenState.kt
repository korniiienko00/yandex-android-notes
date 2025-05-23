package com.korniiienko.notesapp.ui.screens.notes

import com.korniiienko.notesapp.model.Note

sealed interface NotesScreenState {
    data class Success(val notes: List<Note> = listOf()) : NotesScreenState
    object Loading : NotesScreenState
}