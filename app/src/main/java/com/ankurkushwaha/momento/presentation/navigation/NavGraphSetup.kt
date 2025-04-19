package com.ankurkushwaha.momento.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ankurkushwaha.momento.presentation.notes_screen.NoteScreen
import com.ankurkushwaha.momento.presentation.notes_screen.NotesViewModel
import com.ankurkushwaha.momento.presentation.todos_screen.TaskAlertWorkerViewModel
import com.ankurkushwaha.momento.presentation.todos_screen.TaskScreen
import com.ankurkushwaha.momento.presentation.todos_screen.TaskViewModel

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 10:21
 */


@Composable
fun NavGraphSetup(
    modifier: Modifier,
    navController: NavHostController,
    notesViewModel: NotesViewModel,
    taskViewModel: TaskViewModel,
    bottomNavViewModel: BottomNavViewModel,
    taskAlertWorkerViewModel: TaskAlertWorkerViewModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.NotesScreen
    ) {
        composable<Screens.NotesScreen> {
            bottomNavViewModel.updateScreen(Screens.NotesScreen)
            NoteScreen(
                notesViewModel = notesViewModel,
                onAddNoteClick = { notesViewModel.showAddNoteBottomSheet() },
                onNoteClick = { note ->
                    notesViewModel.setSelectedNote(note)
                    notesViewModel.showAddNoteBottomSheet()
                },
                onNoteLongPress = { id, isPin ->
                    notesViewModel.updatePinnedStatus(id, isPin)
                },
                onNoteDismiss = { note ->
                    notesViewModel.deleteNote(note)
                }
            )
        }

        composable<Screens.TaskScreen> {
            bottomNavViewModel.updateScreen(Screens.TaskScreen)
            TaskScreen(
                taskViewModel = taskViewModel,
                onAddTaskClick = {
                    taskViewModel.showAddTaskBottomSheet()
                },
                onTaskCheckBoxClick = { task, isComplete ->
                    taskViewModel.toggleCompleteStatus(task.id, isComplete)
                    if (isComplete && task.workId != null) {
                        taskAlertWorkerViewModel.cancelExistingWorker(task.workId)
                    }
                },
                onTaskDismiss = { task ->
                    taskViewModel.deleteTask(task)
                },
                onTaskCardClick = { task ->
                    taskViewModel.setTask(task)
                    taskViewModel.showAddTaskBottomSheet()
                }
            )
        }
    }
}