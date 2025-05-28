package com.korniiienko.notesapp.di

import android.content.Context
import com.korniiienko.notesapp.data.ThemeRepository
import com.korniiienko.notesapp.data.repository.ThemeRepositoryImpl
import com.korniiienko.notesapp.data.repository.JsonLocalRepository
import com.korniiienko.notesapp.data.repository.LocalRepository
import com.korniiienko.notesapp.data.repository.NetworkRemoteRepository
import com.korniiienko.notesapp.data.repository.RemoteRepository

interface AppContainer {
    val localRepository: LocalRepository
    val remoteRepository: RemoteRepository
    val themeRepository: ThemeRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val remoteRepository: RemoteRepository by lazy {
        NetworkRemoteRepository()
    }

    override val localRepository: LocalRepository by lazy {
        JsonLocalRepository(context = context)
    }
    override val themeRepository: ThemeRepository = ThemeRepositoryImpl(context)
}