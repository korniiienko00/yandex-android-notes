package com.korniiienko.notesapp.ui.screens.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korniiienko.notesapp.data.repository.LocalRepository
import com.korniiienko.notesapp.data.repository.RemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(
    remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
) : ViewModel() {

    val uiState: StateFlow<NotesState> =
        localRepository.notes.map {
            NotesState.Success(it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = TIMEOUT),
            initialValue = NotesState.Loading
        )

    fun processIntent(intent: NotesIntent) {
        when (intent) {
            is NotesIntent.LoadNotes -> loadNotes()
            is NotesIntent.DeleteNote -> deleteNote(intent.noteId)
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            localRepository.load()
        }
    }

    private fun deleteNote(noteId: String) {
        viewModelScope.launch {
            localRepository.deleteNote(noteId)
        }
    }

    companion object {
        const val TIMEOUT = 3_000L
    }
}