package com.ankurkushwaha.momento.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ankurkushwaha.momento.R
import com.ankurkushwaha.momento.receiver.NotificationActionReceiver

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/18 at 11:10
 */

class TaskAlertWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Get data from input parameters if needed
        val createdAt = inputData.getLong("createdAt", -1L)
        val taskMessage = inputData.getString("task_message") ?: "You have a pending task!"

        // Create notification
        sendNotification(createdAt, taskMessage)

        // Return success
        return Result.success()
    }

    private fun sendNotification(createdAt: Long, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create intent to open app when notification is tapped
        val intent =
            applicationContext.packageManager.getLaunchIntentForPackage(applicationContext.packageName)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationId = System.currentTimeMillis().toInt()

        // --- Mark as Complete Action ---
        val markCompleteIntent =
            Intent(applicationContext, NotificationActionReceiver::class.java).apply {
                action = "MARK_COMPLETE"
                putExtra("createdAt", createdAt)
                putExtra("notification_id", notificationId)
            }
        val markCompletePendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            1,
            markCompleteIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // --- Remind Me in 10 Minutes Action ---
        val remindIntent =
            Intent(applicationContext, NotificationActionReceiver::class.java).apply {
                action = "REMIND_LATER"
                putExtra("createdAt", createdAt)
                putExtra("task_message", message)
                putExtra("notification_id", notificationId)
            }
        val remindPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            2,
            remindIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build the notification using the channel ID created in the application class
        val builder = NotificationCompat.Builder(applicationContext, "task_channel")
            .setSmallIcon(R.drawable.note_svg)
            .setContentTitle("Task Reminder")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_done, "Mark Complete", markCompletePendingIntent)
            .addAction(R.drawable.ic_notifications_active, "Remind Later", remindPendingIntent)


        // Show the notification
        notificationManager.notify(notificationId, builder.build())
    }
}