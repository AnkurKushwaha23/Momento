package com.ankurkushwaha.momento.presentation.todos_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankurkushwaha.momento.domain.model.Task
import com.ankurkushwaha.momento.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 11:56
 */

class TaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks = _completedTasks.asStateFlow()

    private val _unCompletedTasks = MutableStateFlow<List<Task>>(emptyList())
    val unCompletedTasks = _unCompletedTasks.asStateFlow()

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask = _selectedTask.asStateFlow()

    private val _addTaskBottomSheet = MutableStateFlow<Boolean>(false)
    val addTaskBottomSheet = _addTaskBottomSheet.asStateFlow()

    private val _timePickerDialog = MutableStateFlow<Boolean>(false)
    val timePickerDialog = _timePickerDialog.asStateFlow()

    private val _timeStamp = MutableStateFlow<Long?>(null)
    val timeStamp = _timeStamp.asStateFlow()

    private val _lastDeletedTask = MutableStateFlow<Task?>(null)
    val lastDeletedTask = _lastDeletedTask.asStateFlow()

    // Add state for showing snackbar
    private val _showUndoSnackbar = MutableStateFlow(false)
    val showUndoSnackbar = _showUndoSnackbar.asStateFlow()

    init {
        getAllCompleteTask()
        getAllUnCompleteTask()
    }

    fun showAddTaskBottomSheet() {
        _addTaskBottomSheet.value = true
    }

    fun hideAddTaskBottomSheet() {
        _addTaskBottomSheet.value = false
        _selectedTask.value = null
        _timeStamp.value = null
    }

    fun showTimerPicker() {
        _timePickerDialog.value = true
    }

    fun hideTimerPicker() {
        _timePickerDialog.value = false
    }

    fun setTask(task: Task) {
        viewModelScope.launch {
            _selectedTask.value = task
        }
    }

    fun setTimeStamp(time: Long) {
        viewModelScope.launch {
            _timeStamp.value = time
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskRepository.insertTask(task)
                hideAddTaskBottomSheet()
            } catch (e: Exception) {
                Log.d("sho", "error ${e.printStackTrace()}")
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                taskRepository.updateTask(task)
                hideAddTaskBottomSheet()
            } catch (e: Exception) {
                Log.d("sho", "error ${e.printStackTrace()}")
            }
        }
    }

    fun toggleCompleteStatus(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            taskRepository.updateCompleteStatus(taskId, isCompleted)
        }
    }

    // Modified delete function
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            // Save the note before deleting
            _lastDeletedTask.value = task
            taskRepository.deleteTask(task)
            hideAddTaskBottomSheet()
            // Trigger snackbar
            _showUndoSnackbar.value = true
        }
    }

    // Add function to undo deletion
    fun undoDelete() {
        viewModelScope.launch {
            _lastDeletedTask.value?.let { task ->
                taskRepository.insertTask(task)
                _lastDeletedTask.value = null
            }
        }
    }

    // Reset snackbar state
    fun snackbarShown() {
        _showUndoSnackbar.value = false
    }

    fun getTaskById(id: Int) {
        viewModelScope.launch {
            _selectedTask.value = taskRepository.getTaskById(id)
        }
    }

    fun getAllCompleteTask() {
        viewModelScope.launch {
            taskRepository.getAllCompleteTask().collect { tasks ->
                _completedTasks.value = tasks
            }
        }
    }

    fun getAllUnCompleteTask() {
        viewModelScope.launch {
            taskRepository.getAllUnCompleteTask().collect { tasks ->
                _unCompletedTasks.value = tasks
            }
        }
    }
}