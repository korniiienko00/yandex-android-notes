package com.korniiienko.notesapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.korniiienko.notesapp.model.AppTheme
import com.korniiienko.notesapp.navigation.AppNavigation

@Composable
fun NotesApp(
    navController: NavHostController = rememberNavController(),
    appTheme: AppTheme,
    modifier: Modifier = Modifier,
) {
    AppNavigation(
        navController = navController,
        appTheme = appTheme,
        modifier = modifier
    )
}