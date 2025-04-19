package com.ankurkushwaha.momento.presentation.notes_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ankurkushwaha.momento.R
import com.ankurkushwaha.momento.domain.model.Notes
import com.ankurkushwaha.momento.presentation.components.SwipeableNoteCard

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 11:41
 */

@Composable
fun NoteScreen(
    notesViewModel: NotesViewModel,
    onAddNoteClick: () -> Unit,
    onNoteClick: (Notes) -> Unit,
    onNoteLongPress: (Int, Boolean) -> Unit,
    onNoteDismiss: (Notes) -> Unit,
    modifier: Modifier = Modifier
) {
    val pinnedNotes = notesViewModel.pinnedNotes.collectAsState()
    val unpinnedNotes = notesViewModel.unpinnedNotes.collectAsState()
    val searchQuery = remember { mutableStateOf("") }

    val filteredPinnedNotes = remember(pinnedNotes.value, searchQuery.value) {
        pinnedNotes.value.filter {
            it.title?.contains(searchQuery.value, ignoreCase = true) == true ||
                    it.description?.contains(searchQuery.value, ignoreCase = true) == true
        }
    }

    val filteredUnpinnedNotes = remember(unpinnedNotes.value, searchQuery.value) {
        unpinnedNotes.value.filter {
            it.title?.contains(searchQuery.value, ignoreCase = true) == true ||
                    it.description?.contains(searchQuery.value, ignoreCase = true) == true
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search notes...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = {
                        if (searchQuery.value.isNotEmpty()) {
                            IconButton(onClick = { searchQuery.value = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }
        }
    ) { paddingValues ->
        if (filteredPinnedNotes.isEmpty() && filteredUnpinnedNotes.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.undraw_add_notes),
                        contentDescription = "Add note",
                        modifier = Modifier.size(160.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (searchQuery.value.isEmpty()) "No notes yet" else "No matching notes found",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Pinned notes section
                if (filteredPinnedNotes.isNotEmpty()) {
                    item {
                        Text(
                            text = "PINNED",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                        )
                    }

                    items(
                        items = filteredPinnedNotes,
                        key = { note: Notes -> note.id }
                    ) { note ->
                        SwipeableNoteCard(
                            note = note,
                            onNoteClick = { note ->
                                // Navigate to note detail/edit screen
                                onNoteClick(note)
                            },
                            onNoteLongPress = { id ->
                                onNoteLongPress(id, false)
                            },
                            onNoteDismiss = onNoteDismiss
                        )
                    }

//                    item {
//                        HorizontalDivider(
//                            modifier = Modifier.padding(vertical = 8.dp),
//                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
//                        )
//                    }
                }

                // Unpinned notes section
                if (filteredUnpinnedNotes.isNotEmpty()) {
                    item {
                        Text(
                            text = "NOTES",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                        )
                    }

                    items(
                        items = filteredUnpinnedNotes,
                        key = { note: Notes -> note.id }
                    ) { note ->
                        SwipeableNoteCard(
                            note = note,
                            onNoteClick = { note ->
                                // Navigate to note detail/edit screen
                                onNoteClick(note)
                            },
                            onNoteLongPress = { id ->
                                onNoteLongPress(id, true)
                            },
                            onNoteDismiss = onNoteDismiss
                        )
                    }
                }
            }
        }
    }
}