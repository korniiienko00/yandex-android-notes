package com.korniiienko.notesapp.ui.screens.add

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korniiienko.notesapp.model.NoteEntity
import com.korniiienko.notesapp.model.toNote
import com.korniiienko.notesapp.data.repository.LocalRepository
import com.korniiienko.notesapp.data.repository.RemoteRepository
import kotlinx.coroutines.launch

class AddNoteViewModel(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : ViewModel() {
    var entryUiState by mutableStateOf(AddNoteState())
        private set

    fun processIntent(intent: AddNoteIntent) {
        when (intent) {
            is AddNoteIntent.UpdateNote -> updateUiState(intent.note)
            AddNoteIntent.SaveNote -> saveNote()
        }
    }

    private fun updateUiState(newNote: NoteEntity) {
        entryUiState = AddNoteState(
            currentNote = newNote,
            isEntryValid = validateInput(newNote)
        )
    }

    private fun validateInput(uiState: NoteEntity = entryUiState.currentNote): Boolean {
        return with(uiState) {
            title.isNotBlank() && content.isNotBlank()
        }
    }

    private fun saveNote() {
        viewModelScope.launch {
            if (validateInput()) {
                localRepository.addNote(note = entryUiState.currentNote.toNote())
                saveToFile()
            }
        }
    }

    private suspend fun saveToFile() {
        localRepository.save()
    }
}