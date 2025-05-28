package com.korniiienko.notesapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korniiienko.notesapp.data.ThemeRepository
import com.korniiienko.notesapp.model.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val themeRepository: ThemeRepository,
) : ViewModel() {

    private val _themeState = MutableStateFlow(AppTheme.SYSTEM)
    val themeState: StateFlow<AppTheme> = _themeState.asStateFlow()

    init {
        viewModelScope.launch {
            val currentTheme = themeRepository.getTheme()
            _themeState.value = currentTheme
        }
    }

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            themeRepository.setTheme(theme)
            _themeState.value = theme
        }
    }
}