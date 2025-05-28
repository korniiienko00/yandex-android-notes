package com.korniiienko.notesapp.di

import android.content.Context
import com.korniiienko.data.local.json.JsonLocalRepository
import com.korniiienko.data.ThemeRepositoryImpl
import com.korniiienko.data.remote.RemoteRepositoryImpl
import com.korniiienko.domain.LocalRepository
import com.korniiienko.domain.RemoteRepository
import com.korniiienko.domain.ThemeRepository

interface AppContainer {
    val localRepository: LocalRepository
    val remoteRepository: RemoteRepository
    val themeRepository: ThemeRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val remoteRepository: RemoteRepository by lazy {
        RemoteRepositoryImpl(
            api = NetworkManager().apiService
        )
    }

    override val localRepository: LocalRepository by lazy {
        JsonLocalRepository(context = context)
    }
    override val themeRepository: ThemeRepository = ThemeRepositoryImpl(context)
}