package com.korniiienko.notesapp.data

import com.korniiienko.notesapp.model.AppTheme

interface ThemeRepository {
    suspend fun getTheme(): AppTheme
    suspend fun setTheme(theme: AppTheme)
}
