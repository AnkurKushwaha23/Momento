package com.ankurkushwaha.momento

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.ankurkushwaha.momento.domain.model.Notes
import com.ankurkushwaha.momento.presentation.components.AdvancedTimePickerExample
import com.ankurkushwaha.momento.presentation.navigation.BottomNavViewModel
import com.ankurkushwaha.momento.presentation.navigation.NavGraphSetup
import com.ankurkushwaha.momento.presentation.navigation.Screens
import com.ankurkushwaha.momento.presentation.notes_screen.NotesBottomSheet
import com.ankurkushwaha.momento.presentation.notes_screen.NotesViewModel
import com.ankurkushwaha.momento.presentation.todos_screen.AddTaskBottomSheet
import com.ankurkushwaha.momento.presentation.todos_screen.TaskAlertWorkerViewModel
import com.ankurkushwaha.momento.presentation.todos_screen.TaskViewModel
import com.ankurkushwaha.momento.ui.theme.MomentoTheme
import com.ankurkushwaha.momento.utils.minutesUntilTimestamp
import com.ankurkushwaha.momento.utils.validateTimeStamp
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val notesViewModel: NotesViewModel by inject()
    private val taskViewModel: TaskViewModel by inject()
    private val taskAlertWorkerViewModel: TaskAlertWorkerViewModel by inject()
    private val bottomNavViewModel: BottomNavViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            MomentoTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                val currentScreen by bottomNavViewModel.currentScreen.collectAsState()

                val showBottomSheet by notesViewModel.showAddNoteSheet.collectAsState()
                val selectedNote by notesViewModel.selectedNote.collectAsState()
                val showNotesUndoSnackbar by notesViewModel.showUndoSnackbar.collectAsState()
                val showTaskUndoSnackbar by taskViewModel.showUndoSnackbar.collectAsState()

                val addTaskBottomSheet by taskViewModel.addTaskBottomSheet.collectAsState()
                val timePickerDialog by taskViewModel.timePickerDialog.collectAsState()
                val timeStamp by taskViewModel.timeStamp.collectAsState()
                val selectedTask by taskViewModel.selectedTask.collectAsState()
                val context = LocalContext.current


                LaunchedEffect(showTaskUndoSnackbar) {
                    if (showTaskUndoSnackbar) {
                        val result = snackbarHostState.showSnackbar(
                            message = "Todo deleted",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                taskViewModel.undoDelete()
                            }

                            SnackbarResult.Dismissed -> {
                                // Todos remains deleted
                            }
                        }
                        taskViewModel.snackbarShown()
                    }
                }

                LaunchedEffect(showNotesUndoSnackbar) {
                    if (showNotesUndoSnackbar) {
                        val result = snackbarHostState.showSnackbar(
                            message = "Note deleted",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )
                        when (result) {
                            SnackbarResult.ActionPerformed -> {
                                notesViewModel.undoDelete()
                            }

                            SnackbarResult.Dismissed -> {
                                // Note remains deleted
                            }
                        }
                        notesViewModel.snackbarShown()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        painter = painterResource(R.drawable.note_svg),
                                        contentDescription = "Notes"
                                    )
                                },
                                label = { Text("Notes") },
                                selected = currentScreen == Screens.NotesScreen,
                                onClick = {
                                    navController.navigate(Screens.NotesScreen)
                                }
                            )
                            NavigationBarItem(
                                icon = { Icon(Icons.Default.Check, contentDescription = "Todos") },
                                label = { Text("Todos") },
                                selected = currentScreen == Screens.TaskScreen,
                                onClick = {
                                    navController.navigate(Screens.TaskScreen)
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Log.d(
                        "PaddingTest",
                        "Top: ${innerPadding.calculateTopPadding()}, Bottom: ${innerPadding.calculateBottomPadding()}"
                    )

                    NavGraphSetup(
                        modifier = Modifier.padding(bottom = 80.dp, top = 25.dp),
                        navController = navController,
                        bottomNavViewModel = bottomNavViewModel,
                        notesViewModel = notesViewModel,
                        taskViewModel = taskViewModel,
                        taskAlertWorkerViewModel = taskAlertWorkerViewModel
                    )

                    if (showBottomSheet) {
                        NotesBottomSheet(
                            onBackClick = { notesViewModel.hideAddNoteBottomSheet() },
                            onDoneClick = { title, description ->
                                val note = Notes(
                                    title = title,
                                    description = description,
                                )
                                notesViewModel.insertNote(note)
                            },
                            onUpdateClick = { note ->
                                notesViewModel.updateNote(note)
                            },
                            note = selectedNote
                        )
                    }

                    if (addTaskBottomSheet) {

                        AddTaskBottomSheet(
                            onDismissRequest = {
                                taskViewModel.hideAddTaskBottomSheet()
                            },
                            onTaskAdded = { task ->
                                if (timeStamp != null) {
                                    val min = minutesUntilTimestamp(timeStamp!!)
                                    val workId = taskAlertWorkerViewModel.initTaskAlertWorker(
                                        min,
                                        task
                                    )
                                    val newTask = task.copy(dueDate = timeStamp, workId = workId)
                                    taskViewModel.insertTask(newTask)
                                } else {
                                    taskViewModel.insertTask(task)
                                }
                            },
                            onTaskUpdate = { task ->
                                if (timeStamp != null) {
                                    val min = minutesUntilTimestamp(timeStamp!!)
                                    if (task.workId != null) {
                                        taskAlertWorkerViewModel.cancelExistingWorker(task.workId)
                                    }
                                    val workId = taskAlertWorkerViewModel.initTaskAlertWorker(
                                        min,
                                        task
                                    )
                                    val newTask = task.copy(dueDate = timeStamp, workId = workId)
                                    taskViewModel.updateTask(newTask)
                                } else {
                                    taskViewModel.updateTask(task)
                                }
                            },
                            onSetAlertClick = {
                                taskViewModel.showTimerPicker()
                            },
                            task = selectedTask,
                            timeStamp = timeStamp
                        )
                    }

                    if (timePickerDialog) {
                        AdvancedTimePickerExample(
                            onConfirm = { timeStamp ->
                                Log.d("violet", "$timeStamp")
                                val timeSt = validateTimeStamp(timeStamp)
                                if (timeSt) {
                                    taskViewModel.setTimeStamp(timeStamp)
                                    taskViewModel.hideTimerPicker()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please select a valid Time",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            onDismiss = {
                                taskViewModel.hideTimerPicker()
                            }
                        )
                    }
                }
            }
        }
    }
}