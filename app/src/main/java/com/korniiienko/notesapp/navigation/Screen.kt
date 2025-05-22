package com.korniiienko.notesapp.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")

    object CreateNote : Screen("create_note")

    data class EditNote(val noteId: String) : Screen("edit_note/{noteId}") {
        companion object {
            const val routePattern = "edit_note/{noteId}"
            const val argName = "noteId"
            fun createRoute(noteId: String) = "edit_note/$noteId"
        }
    }
}
