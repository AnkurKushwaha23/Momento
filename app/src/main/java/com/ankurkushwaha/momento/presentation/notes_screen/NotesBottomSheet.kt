package com.ankurkushwaha.momento.presentation.notes_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ankurkushwaha.momento.domain.model.Notes

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 13:55
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesBottomSheet(
    onBackClick: () -> Unit,
    onDoneClick: (title: String, description: String) -> Unit,
    onUpdateClick: (Notes) -> Unit,
    note: Notes? = null,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(note) {
        note?.let {
            title = it.title.toString()
            description = it.description.toString()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onBackClick,
        sheetState = sheetState,
        dragHandle = null,
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 20.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Back"
                        )
                    }

                    Text(
                        text = if (note != null) "Edit Note" else "New Note",
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(
                        onClick = {
                            if (title.isBlank() && description.isBlank()) {
                                showError = true
                            } else {
                                showError = false
                                if (note != null) {
                                    val notes = Notes(
                                        id = note.id,
                                        title = title.trim(),
                                        description = description.trim(),
                                    )
                                    onUpdateClick(notes)
                                } else {
                                    onDoneClick(title.trim(), description.trim())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "Done"
                        )
                    }
                }

                // Error Text
                if (showError) {
                    Text(
                        text = "Please enter at least a title or description.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // 2. Title TextField
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                )

                // 3. Description TextField
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                )
            }
        }
    }
}


//                // Title TextField (not outlined)
//                TextField(
//                    value = title,
//                    onValueChange = { title = it },
//                    placeholder = { Text("Title") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = MaterialTheme.colorScheme.background,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
//                        disabledContainerColor = MaterialTheme.colorScheme.background,
//                        focusedIndicatorColor = MaterialTheme.colorScheme.background,
//                        unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
//                        disabledIndicatorColor = MaterialTheme.colorScheme.background
//                    )
//                )

// Description TextField (not outlined)
//                TextField(
//                    value = description,
//                    onValueChange = { description = it },
//                    placeholder = { Text("Description") },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .fillMaxHeight(),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = MaterialTheme.colorScheme.background,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
//                        disabledContainerColor = MaterialTheme.colorScheme.background,
//                        focusedIndicatorColor = MaterialTheme.colorScheme.background,
//                        unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
//                        disabledIndicatorColor = MaterialTheme.colorScheme.background
//                    )
//                )