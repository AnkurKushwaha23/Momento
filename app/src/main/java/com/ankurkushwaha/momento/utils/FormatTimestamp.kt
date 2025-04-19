package com.ankurkushwaha.momento.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 10:45
 */

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
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