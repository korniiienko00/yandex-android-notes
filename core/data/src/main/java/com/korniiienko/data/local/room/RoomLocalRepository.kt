package com.korniiienko.data.local.room

import com.korniiienko.domain.LocalRepository
import com.korniiienko.model.Note
import kotlinx.coroutines.flow.Flow

class RoomLocalRepository : LocalRepository {
    override val notes: Flow<List<Note>>
        get() = TODO("Not yet implemented")

    override suspend fun addNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun updateNote(note: Note) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteNote(uid: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getNoteByUid(uid: String): Flow<Note?> {
        TODO("Not yet implemented")
    }

    override suspend fun save() {
        TODO("Not yet implemented")
    }

    override suspend fun load() {
        TODO("Not yet implemented")
    }
}