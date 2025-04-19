package com.ankurkushwaha.momento.presentation.todos_screen

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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.ankurkushwaha.momento.domain.model.Task
import com.ankurkushwaha.momento.presentation.components.SwipeableTaskCard

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 18:26
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TaskScreen(
    taskViewModel: TaskViewModel,
    onAddTaskClick: () -> Unit,
    onTaskCheckBoxClick: (Task,Boolean) -> Unit,
    onTaskCardClick: (Task) -> Unit,
    onTaskDismiss: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedTasks = taskViewModel.completedTasks.collectAsState()
    val unCompletedTasks = taskViewModel.unCompletedTasks.collectAsState()
    val searchQuery = remember { mutableStateOf("") }

    val filteredCompletedTasks = remember(completedTasks.value, searchQuery.value) {
        completedTasks.value.filter {
            it.description.contains(searchQuery.value, ignoreCase = true) == true ||
                    it.description.contains(searchQuery.value, ignoreCase = true) == true
        }
    }

    val filteredUnCompletedTasks = remember(unCompletedTasks.value, searchQuery.value) {
        unCompletedTasks.value.filter {
            it.description.contains(searchQuery.value, ignoreCase = true) == true ||
                    it.description.contains(searchQuery.value, ignoreCase = true) == true
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
                    text = "Todos",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search todos...") },
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
                onClick = onAddTaskClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Todos"
                )
            }
        }
    ) { paddingValues ->
        if (filteredCompletedTasks.isEmpty() && filteredUnCompletedTasks.isEmpty()) {
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
                        painter = painterResource(R.drawable.undraw_todo_list),
                        contentDescription = "Add Todos",
                        modifier = Modifier.size(175.dp)
                    )
                    Text(
                        text = if (searchQuery.value.isEmpty()) "No todos yet" else "No matching todos found",
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
                // UnCompleted task section
                if (filteredUnCompletedTasks.isNotEmpty()) {
                    item {
                        Text(
                            text = "UnCompleted",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                        )
                    }
                    items(
                        items = filteredUnCompletedTasks,
                        key = { task: Task -> task.id }
                    ) { task ->
                        SwipeableTaskCard(
                            task = task,
                            onTaskClick = {task->
                                onTaskCheckBoxClick(task,true)
                            },
                            onTaskDismiss = onTaskDismiss,
                            onTaskCardClick = onTaskCardClick
                        )
                    }
//                    item {
//                        HorizontalDivider(
//                            modifier = Modifier.padding(vertical = 8.dp),
//                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
//                        )
//                    }
                }

                // Completed task section
                if (filteredCompletedTasks.isNotEmpty()) {
                    item {
                        Text(
                            text = "Completed",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                        )
                    }
                    // In your LazyColumn
                    items(
                        items = filteredCompletedTasks,
                        key = { task: Task -> task.id } // Explicitly typing the parameter
                    ) { task ->
                        SwipeableTaskCard(
                            task = task,
                            onTaskClick = {task->
                                onTaskCheckBoxClick(task,false)
                            },
                            onTaskDismiss = onTaskDismiss,
                            onTaskCardClick = onTaskCardClick
                        )
                    }
                }
            }
        }
    }
}