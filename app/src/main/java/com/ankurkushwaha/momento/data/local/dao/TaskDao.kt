package com.ankurkushwaha.momento.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.ankurkushwaha.momento.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 10:54
 */

@Dao
interface TaskDao {

    @Upsert
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("SELECT isCompleted FROM tasks WHERE id = :taskId")
    suspend fun isTaskCompleted(taskId: Int): Boolean

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun updateCompleteStatus(taskId: Int, isCompleted: Boolean)

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE createdAt = :createdAt")
    suspend fun updateCompleteStatusByCreatedAt(createdAt: Long, isCompleted: Boolean)

    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity?

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY priority DESC")
    fun getPendingTasks(): Flow<List<TaskEntity>>
}