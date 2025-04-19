package com.ankurkushwaha.momento.data.local.entity

import androidx.room.TypeConverter
import com.ankurkushwaha.momento.domain.model.Priority

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 10:48
 */
class PriorityConverter {
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name

    @TypeConverter
    fun toPriority(priority: String): Priority = Priority.valueOf(priority)
}