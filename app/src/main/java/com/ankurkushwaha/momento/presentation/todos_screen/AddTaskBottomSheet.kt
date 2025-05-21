package com.ankurkushwaha.momento.presentation.todos_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ankurkushwaha.momento.domain.model.Priority
import com.ankurkushwaha.momento.domain.model.Task
import com.ankurkushwaha.momento.ui.theme.Amber
import com.ankurkushwaha.momento.ui.theme.Green
import com.ankurkushwaha.momento.ui.theme.Red
import com.ankurkushwaha.momento.utils.formatTimestamp
import com.ankurkushwaha.momento.utils.validateTimeStamp

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/17 at 18:01
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    onDismissRequest: () -> Unit,
    onTaskAdded: (Task) -> Unit,
    onTaskUpdate: (Task) -> Unit,
    onSetAlertClick: () -> Unit,
    task: Task? = null,
    timeStamp: Long? = null
) {
    val isEditMode = task != null

    var taskDescription by remember { mutableStateOf(task?.description ?: "") }
    var selectedPriority by remember { mutableStateOf(task?.priority ?: Priority.LOW) }
    var isError by remember { mutableStateOf(false) }
    var isTimerError by remember { mutableStateOf(false) }


//    isTimerError = validateTimeStamp(timeStamp).not()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .padding(bottom = 20.dp) // Add padding at bottom to ensure content isn't cut off
        ) {
            Text(
                text = if (isEditMode) "Edit Task" else "Add Task",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(modifier = Modifier.height(2.dp))
            Spacer(modifier = Modifier.height(10.dp))

            // Priority Selection
            Text(
                text = "Select Priority:",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Priority.entries.forEach { priority ->
                    val priorityColor = when (priority) {
                        Priority.LOW -> Green
                        Priority.MEDIUM -> Amber
                        Priority.HIGH -> Red
                    }

                    val isSelected = selectedPriority == priority
                    val borderColor = if (isSelected) priorityColor else Color.Gray

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) priorityColor.copy(alpha = 0.2f) else Color.Transparent,
                        border = BorderStroke(1.dp, borderColor),
                        onClick = { selectedPriority = priority }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Text(
                                text = priority.name,
                                fontSize = 12.sp,
                                color = if (isSelected) priorityColor else Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Task description field
            OutlinedTextField(
                value = taskDescription,
                onValueChange = {
                    taskDescription = it
                    isError = it.trim().isEmpty()
                },
                label = { Text("Task Description") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text(
                            text = "Task description cannot be empty",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(5.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
            ) {
                if (isEditMode) {
                    if (task.dueDate != null) {
                        Row(
                            modifier = Modifier
                                .clickable { onSetAlertClick() }
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Default.Alarm,
                                contentDescription = "Timer"
                            )

                            Spacer(modifier = Modifier.width(3.dp))

                            val time = validateTimeStamp(task.dueDate)

                            val tColor = if (!time) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }

                            Text(
                                text = if (timeStamp != null) formatTimestamp(timeStamp) else formatTimestamp(
                                    task.dueDate
                                ),
                                color = tColor,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .clickable { onSetAlertClick() }
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Default.Alarm,
                                contentDescription = "Timer"
                            )

                            Spacer(modifier = Modifier.width(3.dp))

                            Text(
                                text = "Set alerts",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    if (timeStamp != null) {
                        Row(
                            modifier = Modifier
                                .clickable { onSetAlertClick() }
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Default.Alarm,
                                contentDescription = "Timer"
                            )

                            Spacer(modifier = Modifier.width(3.dp))

                            Text(
                                text = formatTimestamp(timeStamp),
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .clickable { onSetAlertClick() }
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                imageVector = Icons.Default.Alarm,
                                contentDescription = "Timer"
                            )

                            Spacer(modifier = Modifier.width(3.dp))

                            Text(
                                text = "Set alerts",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Button
            Button(
                onClick = {
                    val currentTimestamp = System.currentTimeMillis()
                    if (taskDescription.trim().isNotEmpty()) {
                        val updatedTask = if (isEditMode) {
                            // Preserve the original task properties when updating
                            task.copy(
                                description = taskDescription.trim(),
                                priority = selectedPriority,
                                createdAt = currentTimestamp
                            )
                        } else {
                            Task(
                                description = taskDescription.trim(),
                                priority = selectedPriority,
                                createdAt = currentTimestamp
                            )
                        }
                        if (isEditMode) {
                            onTaskUpdate(updatedTask)
                        } else {
                            onTaskAdded(updatedTask)
                        }
                        onDismissRequest()
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier.align(Alignment.End),
                enabled = !isError
            ) {
                Text(if (isEditMode) "Update Task" else "Add Task")
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun TaskBottomSheetPreview() {
//    val task = Task(
//        id = 1,
//        description = "Buy groceries",
//        isCompleted = true,
//        priority = Priority.HIGH
//    )
//
//    MaterialTheme {
//        AddTaskBottomSheet(
//            onDismissRequest = {},
//            onTaskAdded = {},
//            onTaskUpdate = {},
//            onSetAlertClick = {},
//            task = task
//        )
//    }
//}