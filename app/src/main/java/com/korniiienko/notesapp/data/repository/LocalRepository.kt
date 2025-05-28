package com.korniiienko.notesapp.data.repository

import android.content.Context
import com.korniiienko.notesapp.data.FileNotebook
import com.korniiienko.notesapp.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

interface LocalRepository {
    val notes: Flow<List<Note>>
    suspend fun addNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(uid: String)
    suspend fun getNoteByUid(uid: String): Flow<Note?>
    suspend fun save()
    suspend fun load()
}

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

class JsonLocalRepository(
    context: Context,
) : LocalRepository {
    private val fileNotebook = FileNotebook(context)

    override val notes: Flow<List<Note>> = fileNotebook.notes

    override suspend fun addNote(note: Note) {
        fileNotebook.addNote(note)
    }

    override suspend fun updateNote(note: Note) {
        fileNotebook.updateNote(note)
    }

    override suspend fun deleteNote(uid: String) {
        fileNotebook.deleteNote(uid)
    }

    override suspend fun getNoteByUid(uid: String): Flow<Note> {
        return fileNotebook.getNoteByUid(uid)
    }

    override suspend fun save() {
        fileNotebook.saveToFile()
    }

    override suspend fun load() {
        fileNotebook.loadFromFile()
    }
}
