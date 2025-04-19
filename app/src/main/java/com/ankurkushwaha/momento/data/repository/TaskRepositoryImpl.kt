package com.ankurkushwaha.momento.data.repository

import android.util.Log
import com.ankurkushwaha.momento.data.local.dao.TaskDao
import com.ankurkushwaha.momento.data.mapper.toDomain
import com.ankurkushwaha.momento.data.mapper.toEntity
import com.ankurkushwaha.momento.domain.model.Task
import com.ankurkushwaha.momento.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {
    override suspend fun insertTask(task: Task) {
        val taskEntity = task.toEntity()
        taskDao.insertTask(taskEntity)
    }

    override suspend fun updateTask(task: Task) {
        val taskEntity = task.toEntity()
        taskDao.updateTask(taskEntity)
    }

    override suspend fun deleteTask(task: Task) {
        val taskEntity = task.toEntity()
        taskDao.deleteTask(taskEntity)
    }

    override suspend fun isTaskCompleted(taskId: Int): Boolean {
        return taskDao.isTaskCompleted(taskId)
    }

    override suspend fun updateCompleteStatus(taskId: Int, isCompleted: Boolean) {
        taskDao.updateCompleteStatus(taskId,isCompleted)
    }

    override suspend fun updateCompleteStatusByCreatedAt(
        createdAt: Long,
        isCompleted: Boolean
    ) {
        Log.d("xxx","updateCompleteStatusByCreatedAt called with $createdAt and $isCompleted")
        taskDao.updateCompleteStatusByCreatedAt(createdAt,isCompleted)
    }

    override suspend fun getTaskById(id: Int): Task {
        return taskDao.getTaskById(id)!!.toDomain()
    }

    override fun getAllTask(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { task ->
            task.map { it.toDomain() }
        }
    }

    override fun getAllCompleteTask(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { task ->
            task.filter { it.isCompleted }
                .map { it.toDomain() }
        }
    }

    override fun getAllUnCompleteTask(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { task ->
            task.filter { !(it.isCompleted) }
                .map { it.toDomain() }
        }
    }
}