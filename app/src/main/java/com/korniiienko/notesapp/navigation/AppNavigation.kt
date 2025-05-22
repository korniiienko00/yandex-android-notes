package com.korniiienko.notesapp.navigation


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.korniiienko.notesapp.ui.screens.add.AddNoteScreen
import com.korniiienko.notesapp.ui.screens.edit.EditNoteScreen
import com.korniiienko.notesapp.ui.screens.notes.NotesScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            NotesScreen(
                onClickAdd = {
                    navController.navigate(Screen.CreateNote.route)
                },
                onClickOpenNote = { noteId ->
                    navController.navigate(Screen.EditNote.createRoute(noteId))
                },
                modifier = Modifier
            )
        }

        composable(
            route = Screen.EditNote.routePattern,
            arguments = listOf(
                navArgument(Screen.EditNote.argName) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString(Screen.EditNote.argName)
                ?: throw IllegalArgumentException("noteId parameter wasn't found")
            EditNoteScreen(
                noteId = noteId,
                navigateBack = { navController.navigateUp() },
                modifier = Modifier
            )
        }

        composable(route = Screen.CreateNote.route) {
            AddNoteScreen(
                navigateBack = { navController.navigateUp() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
