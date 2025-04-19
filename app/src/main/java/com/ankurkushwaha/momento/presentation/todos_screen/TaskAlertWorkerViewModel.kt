package com.ankurkushwaha.momento.presentation.todos_screen

import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ankurkushwaha.momento.domain.model.Task
import com.ankurkushwaha.momento.worker.TaskAlertWorker
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/18 at 11:19
 */

class TaskAlertWorkerViewModel(
    private val workManager: WorkManager
) : ViewModel() {

    fun cancelExistingWorker(workId: String) {
        workManager.cancelWorkById(UUID.fromString(workId))
    }

    fun initTaskAlertWorker(minutes: Long, task: Task): String {
        val inputData = workDataOf(
            "createdAt" to task.createdAt,
            "task_message" to task.description
        )
        val taskAlertRequest = OneTimeWorkRequestBuilder<TaskAlertWorker>()
            .setInitialDelay(minutes, TimeUnit.MINUTES)
            .setInputData(inputData)
            .addTag("TaskAlertWorkerTag")
            .build()

        // Enqueue the work
        workManager.enqueue(taskAlertRequest)

        // Return the WorkRequest ID to save it in your task
        return taskAlertRequest.id.toString()
    }
}