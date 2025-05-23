package com.korniiienko.notesapp.ui.screens.notes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korniiienko.notesapp.repository.LocalRepository
import com.korniiienko.notesapp.repository.RemoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NotesViewModel(
    remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
): ViewModel()  {
    val uiState: StateFlow<NotesScreenState> =
        localRepository.notes.map {
            NotesScreenState.Success(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = TIMEOUT),
            initialValue = NotesScreenState.Loading
        )

    suspend fun deleteNoteById(noteUid: String) {
        localRepository.deleteNote(uid = noteUid)
    }

    suspend fun loadFromFile() {
        localRepository.load()
    }


    companion object {
        const val TIMEOUT = 3_000L
    }
}