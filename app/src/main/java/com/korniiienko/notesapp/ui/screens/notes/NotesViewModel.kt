package com.korniiienko.notesapp.ui.screens.notes

import androidx.lifecycle.ViewModel
import com.korniiienko.notesapp.repository.LocalRepository
import com.korniiienko.notesapp.repository.RemoteRepository

class NotesViewModel(
    remoteRepository: RemoteRepository,
    localRepository: LocalRepository
): ViewModel()  {
}