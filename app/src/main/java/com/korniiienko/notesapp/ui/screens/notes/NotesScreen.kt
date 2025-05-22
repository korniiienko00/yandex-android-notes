package com.korniiienko.notesapp.ui.screens.notes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.korniiienko.notesapp.ViewModelProvider

@Composable
fun NotesScreen(
    onClickAdd: () -> Unit,
    onClickOpenNote: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel(factory = ViewModelProvider.Factory)
) {

}