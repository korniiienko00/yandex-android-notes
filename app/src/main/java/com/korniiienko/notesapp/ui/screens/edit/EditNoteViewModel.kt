package com.korniiienko.notesapp.ui.screens.edit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korniiienko.notesapp.model.NoteEntity
import com.korniiienko.notesapp.model.toNote
import com.korniiienko.notesapp.model.toUiState
import com.korniiienko.notesapp.data.repository.LocalRepository
import com.korniiienko.notesapp.ui.screens.add.NoteEntryState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditNoteViewModel(
    savedStateHandle: SavedStateHandle,
    private val localRepository: LocalRepository
) : ViewModel() {
    var entryUiState by mutableStateOf(NoteEntryState())
        private set

    fun loadNoteByUid(uid: String) {
        viewModelScope.launch {
            entryUiState = localRepository.getNoteByUid(uid = uid)
                .filterNotNull()
                .map {
                    NoteEntryState(
                        currentNote = it.toUiState(),
                        isEntryValid = true
                    )
                }
                .first()
        }
    }

    fun updateUiState(newNote: NoteEntity) {
        entryUiState = NoteEntryState(
            currentNote = newNote,
            isEntryValid = validateInput(newNote)
        ).also {
            Log.e("newNote","item updated. Updated note is $newNote")
        }
    }

    private fun validateInput(uiEntry: NoteEntity = entryUiState.currentNote) = with(uiEntry) {
        title.isNotBlank() && content.isNotBlank()
    }

    suspend fun updateItem() {
        if (validateInput()) {
            localRepository.updateNote(
                note = entryUiState.currentNote.toNote()
            )
        }
    }

    suspend fun deleteItem() {
        localRepository.deleteNote(
            uid = entryUiState.currentNote.toNote().uid
        )
    }
}