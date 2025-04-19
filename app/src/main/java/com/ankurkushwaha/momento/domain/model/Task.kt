package com.ankurkushwaha.momento.domain.model

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 10:37
 */
data class Task(
    val id: Int = 0,
    val description: String,
    val isCompleted: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null,
    val workId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
