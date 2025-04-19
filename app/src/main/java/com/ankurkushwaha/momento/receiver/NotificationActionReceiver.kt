package com.ankurkushwaha.momento.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ankurkushwaha.momento.domain.repository.TaskRepository
import com.ankurkushwaha.momento.worker.TaskAlertWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext
import java.util.concurrent.TimeUnit

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/19 at 09:22
 */

class NotificationActionReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val createdAt = intent.getLongExtra("createdAt", -1L)
        val notificationId = intent.getIntExtra("notification_id", -1)
        val taskMessage =
            intent.getStringExtra("task_message") ?: "Reminder: You have a pending task!"

        // Get TaskViewModel from Koin manually
        val taskRepository: TaskRepository = GlobalContext.get().get()

        when (intent.action) {
            "MARK_COMPLETE" -> {
                // Handle mark complete (e.g., dismiss notification, update task as done)
                if (createdAt != -1L) {
                    CoroutineScope(Dispatchers.IO).launch {
                        taskRepository.updateCompleteStatusByCreatedAt(createdAt, true)
                    }
                }

                // Cancel the notification
                if (notificationId != -1) {
                    val manager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    manager.cancel(notificationId)
                }
            }

            "REMIND_LATER" -> {
                // Handle remind later (schedule a new notification after 10 minutes)
                val inputData = workDataOf(
                    "createdAt" to createdAt,
                    "task_message" to taskMessage
                )
                val workRequest = OneTimeWorkRequestBuilder<TaskAlertWorker>()
                    .setInitialDelay(10, TimeUnit.MINUTES)
                    .setInputData(inputData)
                    .addTag("TaskAlertWorkerTag")
                    .build()
                WorkManager.Companion.getInstance(context).enqueue(workRequest)
                // Cancel the notification
                if (notificationId != -1) {
                    val manager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    manager.cancel(notificationId)
                }
            }
        }
    }
}