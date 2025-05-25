package com.ankurkushwaha.momento.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 10:45
 */
@RequiresApi(Build.VERSION_CODES.O)
fun formatTimestamp(timestamp: Long): String {
    val dateTime = Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
    return dateTime.format(formatter)
}

fun minutesUntilTimestamp(targetTimestamp: Long): Long {
    val currentTimeMillis = System.currentTimeMillis()
    val differenceMillis = targetTimestamp - currentTimeMillis

    // Ensure we don't return negative values
    return if (differenceMillis > 0) {
        differenceMillis / (60 * 1000)
    } else {
        0
    }
}

fun validateTimeStamp(timestamp: Long): Boolean {
    val currentTimeMillis = System.currentTimeMillis()
    return timestamp > currentTimeMillis
}
