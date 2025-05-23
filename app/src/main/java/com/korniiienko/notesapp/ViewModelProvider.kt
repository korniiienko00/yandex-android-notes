package com.korniiienko.notesapp

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.korniiienko.notesapp.ui.screens.add.NoteViewModel
import com.korniiienko.notesapp.ui.screens.edit.EditNoteViewModel
import com.korniiienko.notesapp.ui.screens.notes.NotesViewModel

object ViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            NotesViewModel(
                remoteRepository = notesApplication().container.remoteRepository,
                localRepository = notesApplication().container.localRepository
            )
        }

        initializer {
            NoteViewModel(
                remoteRepository = notesApplication().container.remoteRepository,
            )
        }

        initializer {
            EditNoteViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                remoteRepository = notesApplication().container.remoteRepository,
            )
        }
    }
}

fun CreationExtras.notesApplication(): NotesApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NotesApplication)