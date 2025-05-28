package com.korniiienko.notesapp.ui.screens.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.korniiienko.notesapp.R
import com.korniiienko.notesapp.ViewModelProvider
import com.korniiienko.notesapp.model.Note
import com.korniiienko.notesapp.navigation.Screen
import com.korniiienko.notesapp.ui.shared.LoadingCircle
import com.korniiienko.notesapp.ui.shared.SwipeCard
import com.korniiienko.notesapp.ui.shared.TopAppBar
import com.korniiienko.notesapp.ui.theme.Spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    onClickAddNote: () -> Unit,
    onClickOpenNote: (String) -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    viewModel: NotesViewModel = viewModel(factory = ViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadFromFile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                name = Screen.MainNotes.name,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickAddNote,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(Spacing.large)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = Icons.Default.Add.name
                )
            }
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValue ->

        when (val state = uiState) {
            is NotesScreenState.Loading -> {
                LoadingCircle()
            }

            is NotesScreenState.Success -> {
                HabitContent(
                    notes = state.notes,
                    onClickNote = {
                        coroutineScope.launch {
                            onClickOpenNote.invoke(it)
                        }
                    },
                    onSwipeDelete = {
                        coroutineScope.launch {
                            viewModel.deleteNoteById(it)
                        }
                    },
                    onSwipeEdit = {
                        coroutineScope.launch {
                            onClickOpenNote.invoke(it)
                        }
                    },
                    modifier = modifier,
                    contentPadding = paddingValue
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitContent(
    notes: List<Note>,
    onClickNote: (String) -> Unit,
    onSwipeDelete: (String) -> Unit,
    onSwipeEdit: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    if (notes.isEmpty()) {
        Text(
            text = stringResource(R.string.no_items),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.fillMaxWidth(),
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(270.dp),
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.Top
        ) {
            items(items = notes, key = { it.uid }) { note ->
                SwipeCard(
                    note = note,
                    onActionDelete = {
                        onSwipeDelete(note.uid)
                    },
                    onActionEdit = {
                        onSwipeEdit(note.uid)
                    },
                    onClickNote = {
                        onClickNote(note.uid)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = Spacing.small,
                            horizontal = Spacing.medium
                        )
                )
            }
        }
    }
}

