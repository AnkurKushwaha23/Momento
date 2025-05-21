package com.ankurkushwaha.momento.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ankurkushwaha.momento.domain.model.Priority
import com.ankurkushwaha.momento.domain.model.Task
import com.ankurkushwaha.momento.ui.theme.Amber
import com.ankurkushwaha.momento.ui.theme.Green
import com.ankurkushwaha.momento.ui.theme.Red
import com.ankurkushwaha.momento.utils.formatTimestamp
import com.ankurkushwaha.momento.utils.validateTimeStamp

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 10:53
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SwipeableTaskCard(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onTaskDismiss: (Task) -> Unit,
    onTaskCardClick: (Task) -> Unit,
) {
    var dismissState = rememberDismissState(
        confirmStateChange = { value ->
            if (value == DismissValue.DismissedToEnd || value == DismissValue.DismissedToStart) {
                onTaskDismiss(task)
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
            TaskCard(
                task = task,
                onTaskClick = onTaskClick,
                onTaskCardClick = onTaskCardClick
            )
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskCard(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onTaskCardClick: (Task) -> Unit,
) {
    val priorityColor = when (task.priority) {
        Priority.LOW -> Green
        Priority.MEDIUM -> Amber
        Priority.HIGH -> Red
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onTaskCardClick(task) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconToggleButton(
                checked = task.isCompleted,
                onCheckedChange = { onTaskClick(task) }
            ) {
                val icon = if (task.isCompleted)
                    Icons.Filled.CheckCircle
                else
                    Icons.Outlined.RadioButtonUnchecked

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (task.isCompleted) Color.Gray  else priorityColor
                )
            }

            Spacer(modifier = Modifier.width(2.dp))

            Column {
                Text(
                    text = task.description,
                    color = if (task.isCompleted) Color.Gray  else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                if (task.dueDate != null){
                    Spacer(modifier = Modifier.height(2.dp))
                    val time = validateTimeStamp(task.dueDate)

                    val tColor = if (task.isCompleted){
                        Color.Gray
                    }else if (!time){
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }

                    Text(
                        text = formatTimestamp(task.dueDate),
                        color = tColor,
                        style = MaterialTheme.typography.labelSmall.copy(
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TaskCardPreview() {
//    val task = Task(
//        id = 1,
//        description = "Buy groceries Here's the converted VectorDrawable version based on your SVG",
//        isCompleted = true,
//        priority = Priority.HIGH,
//        dueDate = 1744950780000
//    )
//
//    MaterialTheme {
////        TaskCard(task = task, onTaskClick = {})
//        SwipeableTaskCard(task = task, onTaskClick = {}, onTaskDismiss = {}, onTaskCardClick = {})
//    }
//}
