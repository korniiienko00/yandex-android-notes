package com.korniiienko.data.remote

import com.korniiienko.model.remote.UidModel
import com.korniiienko.data.remote.model.PatchNotesRequest
import com.korniiienko.data.remote.model.SingleNoteRequest
import com.korniiienko.domain.RemoteRepository
import com.korniiienko.model.Note
import com.korniiienko.model.remote.NetworkError
import com.korniiienko.model.remote.NetworkResult
import org.slf4j.LoggerFactory
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

class RemoteRepositoryImpl(
    private val api: RemoteApiService,
) : RemoteRepository {
    private val logger = LoggerFactory.getLogger(RemoteRepositoryImpl::class.java)
    private var revision: Int = 0

    override suspend fun getNotes(): NetworkResult<List<Note>> {
        return try {
            logger.debug("Fetching notes from backend")
            val response = api.getNotes()
            revision = response.revision
            logger.info("Successfully fetched ${response.list.size} notes, new revision: $revision")
            NetworkResult.Success(response.list.map { it.toModel() })
        } catch (e: Exception) {
            logger.error("Failed to fetch notes", e)
            NetworkResult.Failure(e.mapToNetworkError())
        }
    }

    override suspend fun getNote(noteUid: String): NetworkResult<Note> {
        return try {
            logger.debug("Fetching note with UID: $noteUid")
            val response = api.getNote(noteUid = noteUid)
            revision = response.revision
            logger.info("Successfully fetched note ${response.element.id}, new revision: $revision")
            NetworkResult.Success(response.element.toModel())
        } catch (e: Exception) {
            logger.error("Failed to fetch note $noteUid", e)
            NetworkResult.Failure(e.mapToNetworkError())
        }
    }

    override suspend fun addNote(note: Note, deviceId: String): NetworkResult<UidModel> {
        return try {
            val currentRevision = api.getNotes().revision
            logger.debug("Adding new note: ${note.title} (${note.uid})")

            val response = api.addNote(
                revision = currentRevision,
                request = SingleNoteRequest(note.toRemoteDto(deviceId))
            )
            revision = response.revision
            logger.info("Successfully added note ${response.element.id}, new revision: $revision")
            NetworkResult.Success(UidModel(response.element.id))
        } catch (e: Exception) {
            logger.error("Failed to add note ${note.uid}", e)
            NetworkResult.Failure(e.mapToNetworkError())
        }
    }

    override suspend fun updateNote(note: Note, deviceId: String): NetworkResult<UidModel> {
        return try {
            val currentRevision = api.getNotes().revision
            logger.debug("Updating note: ${note.title} (${note.uid})")

            try {
                val response = api.updateNote(
                    revision = currentRevision,
                    noteUid = note.uid,
                    request = SingleNoteRequest(note.toRemoteDto(deviceId))
                )

                revision = response.revision
                logger.info("Successfully updated note ${response.element.id}, new revision: $revision")
                NetworkResult.Success(UidModel(response.element.id))
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    logger.warn("Note not found (404), trying to add as new")
                    addNote(note, deviceId)
                } else {
                    throw e
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to update note ${note.uid}", e)
            NetworkResult.Failure(e.mapToNetworkError())
        }
    }

    override suspend fun deleteNote(noteUid: String): NetworkResult<Unit> {
        return try {
            logger.debug("Deleting note with UID: $noteUid")
            val currentRevision = api.getNotes().revision
            val response = api.deleteNote(revision = currentRevision, noteUid = noteUid)
            revision = response.revision
            logger.info("Successfully deleted note $noteUid, new revision: $revision")
            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            logger.error("Failed to delete note $noteUid", e)
            NetworkResult.Failure(e.mapToNetworkError())
        }
    }

    override suspend fun patchNotes(
        notes: List<Note>,
        deviceId: String,
    ): NetworkResult<List<Note>> {
        return try {
            logger.debug("Patching ${notes.size} notes")
            val currentRevision = api.getNotes().revision
            val response = api.patchNotes(
                revision = currentRevision,
                request = PatchNotesRequest(notes.map { it.toRemoteDto(deviceId) })
            )
            revision = response.revision
            logger.info("Successfully patched notes, new revision: $revision")
            NetworkResult.Success(response.list.map { it.toModel() })
        } catch (e: Exception) {
            logger.error("Failed to patch notes", e)
            NetworkResult.Failure(e.mapToNetworkError())
        }
    }

    override suspend fun getNotesWithThreshold(generateFailsThreshold: Int?): NetworkResult<List<Note>> {
        return try {
            logger.debug("Fetching notes with fail threshold: $generateFailsThreshold")
            val response = api.getNotes(generateFailsThreshold = generateFailsThreshold)
            revision = response.revision
            logger.info("Successfully fetched ${response.list.size} notes with threshold, new revision: $revision")
            NetworkResult.Success(response.list.map { it.toModel() })
        } catch (e: Exception) {
            logger.error("Failed to fetch notes with threshold", e)
            NetworkResult.Failure(e.mapToNetworkError())
        }
    }

    override suspend fun clearAllNotes() {
        try {
            var currentRevision = api.getNotes().revision
            api.getNotes().list.forEach { note ->
                val response = api.deleteNote(
                    revision = currentRevision,
                    noteUid = note.id
                )
                currentRevision = response.revision
            }
        } catch (e: Exception) {
            logger.error("Failed to clear notes", e)
        }
    }

    private fun Throwable.mapToNetworkError(): Throwable {
        return when (this) {
            is SocketTimeoutException,
            is SSLHandshakeException,
            is IOException -> NetworkError("Network error occurred", this)

            is HttpException -> when (code()) {
                400 -> NetworkError("Bad request", this)
                401 -> NetworkError("Unauthorized", this)
                404 -> NetworkError("Not found", this)
                409 -> NetworkError("Conflict", this)
                413 -> NetworkError("Payload too large", this)
                429 -> NetworkError("Too many requests", this)
                in 500..599 -> NetworkError("Server error", this)
                else -> NetworkError("HTTP error", this)
            }

            else -> NetworkError("Unknown network error", this)
        }
    }
}