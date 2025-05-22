package com.korniiienko.notesapp.data

import kotlinx.coroutines.flow.Flow
import com.korniiienko.notesapp.model.Note

interface NotesRepository {
    val notes: Flow<List<Note>>
    fun addNote(note: Note)
    fun getNoteByUid(uid: String): Flow<Note>
    fun updateNote(note: Note)
    fun deleteNote(uid: String)
    fun saveToFile()
    fun loadFromFile()
}