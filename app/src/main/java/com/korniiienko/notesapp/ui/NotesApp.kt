package com.korniiienko.notesapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.korniiienko.notesapp.navigation.AppNavigation

@Composable
fun NotesApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    AppNavigation(
        navController = navController,
        modifier = modifier
    )
}