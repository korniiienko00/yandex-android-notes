package com.korniiienko.data.local

import android.content.Context
import com.korniiienko.data.FileNotebook
import com.korniiienko.domain.LocalRepository
import com.korniiienko.model.Note
import kotlinx.coroutines.flow.Flow

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
