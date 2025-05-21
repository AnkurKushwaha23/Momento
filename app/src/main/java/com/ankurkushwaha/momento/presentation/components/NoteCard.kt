package com.ankurkushwaha.momento.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ankurkushwaha.momento.domain.model.Notes
import com.ankurkushwaha.momento.utils.formatTimestamp

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 10:33
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableNoteCard(
    note: Notes,
    onNoteClick: (Notes) -> Unit,
    onNoteLongPress: (Int) -> Unit,
    onNoteDismiss: (Notes) -> Unit
) {
    var dismissState = rememberDismissState(
        confirmStateChange = { value ->
            if (value == DismissValue.DismissedToEnd || value == DismissValue.DismissedToStart) {
                onNoteDismiss(note)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {

            val color = MaterialTheme.colorScheme.error
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(color),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {

                val scale by animateFloatAsState(
                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 7.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Task",
                        tint = Color.White,
//                        modifier = Modifier.scale(scale)
                    )
                }
            }
        },
        dismissContent = {
            NoteCard(note = note, onNoteClick = onNoteClick, onNoteLongPress = onNoteLongPress)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteCard(
    note: Notes,
    onNoteClick: (Notes) -> Unit,
    onNoteLongPress: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onNoteLongPress(note.id) // <- your long press handler
                    },
                    onTap = {
                        onNoteClick(note) // <- your normal click handler
                    }
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // Pinned Label
            if (note.isPinned) {
                Text(
                    text = "📌 Pinned",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Title or Description
            if (!note.title.isNullOrBlank()) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (!note.description.isNullOrBlank()) {
                Text(
                    text = note.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Timestamp
            Text(
                text = formatTimestamp(note.timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun NoteCardPreview() {
//    val sampleNote = Notes(
//        id = 1,
//        title = "Meeting Notes",
//        description = "Discuss project deliverables and timelines.",
//        timestamp = System.currentTimeMillis(),
//        isPinned = true
//    )
//
//    MaterialTheme {
////        NoteCard(note = sampleNote, onNoteClick = {}, onNoteLongPress = {})
//        SwipeableNoteCard(note = sampleNote, onNoteClick = {}, onNoteLongPress = {}, onNoteDismiss = {})
//    }
//}

