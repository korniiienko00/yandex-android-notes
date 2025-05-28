package com.korniiienko.data.remote

import com.korniiienko.data.FileNotebook
import com.korniiienko.domain.RemoteRepository
import com.korniiienko.model.Note
import org.slf4j.LoggerFactory

class RemoteRepositoryImpl: RemoteRepository {
    private val logger = LoggerFactory.getLogger(FileNotebook::class.java)

    // ---------- Заглушки для сетевых операций ----------

    override suspend fun addNoteToBackend(note: Note) {
        logger.info("Заглушка: отправка заметки на бэкенд: ${note.title}")
        // здесь будет вызов API в будущем
    }

    override suspend fun getNotesFromBackend(): List<Note> {
        logger.info("Заглушка: загрузка заметок с бэкенда")
        return emptyList()
    }

    override suspend fun deleteNoteFromBackend(uid: String) {
        logger.info("Заглушка: удаление заметки с бэкенда UID=$uid")
    }
}

