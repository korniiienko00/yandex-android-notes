package com.korniiienko.domain

import com.korniiienko.model.Note
import com.korniiienko.model.remote.NetworkResult
import com.korniiienko.model.remote.UidModel

interface RemoteRepository {
    suspend fun getNotes(): NetworkResult<List<Note>>
    suspend fun getNote(noteUid: String): NetworkResult<Note>
    suspend fun addNote(note: Note, deviceId: String): NetworkResult<UidModel>
    suspend fun updateNote(note: Note, deviceId: String): NetworkResult<UidModel>
    suspend fun deleteNote(noteUid: String): NetworkResult<Unit>
    suspend fun patchNotes(notes: List<Note>, deviceId: String): NetworkResult<List<Note>>
    suspend fun getNotesWithThreshold(generateFailsThreshold: Int?): NetworkResult<List<Note>>
    suspend fun clearAllNotes()
}
