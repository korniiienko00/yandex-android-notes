package com.korniiienko.notesapp.ui.screens.add

import android.content.Context
import android.util.Log
import androidx.annotation.RequiresApi
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
    var entryUiState by mutableStateOf(NoteEntryState())
        private set

    fun updateUiState(newNote: NoteEntity) {
        entryUiState = NoteEntryState(
            currentNote = newNote,
            isEntryValid = validateInput(newNote)
        ).also {
            Log.e("newNote","new item updated. New note is $newNote")
        }
    }

    private fun validateInput(uiState: NoteEntity = entryUiState.currentNote): Boolean {
        return with(uiState) {
            title.isNotBlank() && content.isNotBlank()
        }
    }

    fun saveItem() {
        viewModelScope.launch {
            if (validateInput()) {
                localRepository.addNote(note = entryUiState.currentNote.toNote())
            }
            saveToFile()
        }
    }

    suspend fun saveToFile() {
        localRepository.save()
    }
}