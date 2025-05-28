package com.korniiienko.notesapp.ui.screens.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korniiienko.data.LocalRepository
import com.korniiienko.notesapp.ui.screens.NoteEntity
import com.korniiienko.notesapp.ui.screens.toNote
import com.korniiienko.notesapp.ui.screens.toUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EditNoteViewModel(
    savedStateHandle: SavedStateHandle,
    private val localRepository: LocalRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(EditNoteState())
    val state: StateFlow<EditNoteState> = _state

    fun processIntent(intent: EditNoteIntent) {
        when (intent) {
            is EditNoteIntent.LoadNote -> loadNote(intent.uid)
            is EditNoteIntent.UpdateNoteEntity -> updateNoteState(intent.note)
            EditNoteIntent.UpdateNote -> updateNote()
            EditNoteIntent.DeleteNote -> deleteNote()
            EditNoteIntent.ResetDeleteState -> resetDeleteState()
        }
    }

    private fun loadNote(uid: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            localRepository.getNoteByUid(uid = uid)
                .filterNotNull()
                .map { note ->
                    _state.value.copy(
                        currentNote = note.toUiState(),
                        isEntryValid = true,
                        isLoading = false
                    )
                }
                .first()
                .let { newState ->
                    _state.value = newState
                }
        }
    }

    private fun updateNoteState(newNote: NoteEntity) {
        _state.value = _state.value.copy(
            currentNote = newNote,
            isEntryValid = validateInput(newNote)
        )
    }

    private fun validateInput(uiEntry: NoteEntity = _state.value.currentNote): Boolean {
        return with(uiEntry) {
            title.isNotBlank() && content.isNotBlank()
        }
    }

    private fun updateNote() {
        viewModelScope.launch {
            if (validateInput()) {
                localRepository.updateNote(
                    note = _state.value.currentNote.toNote()
                )
            }
        }
    }

    private fun deleteNote() {
        viewModelScope.launch {
            localRepository.deleteNote(
                uid = _state.value.currentNote.toNote().uid
            )
            _state.value = _state.value.copy(isDeleted = true)
        }
    }

    private fun resetDeleteState() {
        _state.value = _state.value.copy(isDeleted = false)
    }
}