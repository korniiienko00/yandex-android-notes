package com.korniiienko.notesapp

import android.content.Context
import com.korniiienko.notesapp.repository.LocalRepository
import com.korniiienko.notesapp.repository.RemoteRepository

interface AppContainer {
    val localRepository: LocalRepository
    val remoteRepository: RemoteRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val remoteRepository: RemoteRepository by lazy {
        RemoteRepository()
    }

    override val localRepository: LocalRepository by lazy {
        LocalRepository()
    }
}