package com.ankurkushwaha.momento.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ankurkushwaha.momento.domain.model.Priority

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 10:47
 */

@Entity(tableName = "tasks")
@TypeConverters(PriorityConverter::class)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null,
    val workId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
