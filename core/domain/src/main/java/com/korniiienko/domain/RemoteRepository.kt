package com.korniiienko.domain

import com.korniiienko.model.Note

interface RemoteRepository {
    suspend fun addNoteToBackend(note: Note)
    suspend fun getNotesFromBackend(): List<Note>
    suspend fun deleteNoteFromBackend(uid: String)
}