package com.ankurkushwaha.momento.domain.repository

import com.ankurkushwaha.momento.domain.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 11:08
 */

interface TaskRepository {
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun isTaskCompleted(taskId: Int): Boolean
    suspend fun updateCompleteStatus(taskId: Int, isCompleted: Boolean)
    suspend fun updateCompleteStatusByCreatedAt(createdAt: Long, isCompleted: Boolean)
    suspend fun getTaskById(id: Int):Task
    fun getAllTask():Flow<List<Task>>
    fun getAllCompleteTask():Flow<List<Task>>
    fun getAllUnCompleteTask():Flow<List<Task>>
}