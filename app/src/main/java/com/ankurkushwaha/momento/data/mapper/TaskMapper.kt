package com.ankurkushwaha.momento.data.mapper

import com.ankurkushwaha.momento.data.local.entity.TaskEntity
import com.ankurkushwaha.momento.domain.model.Task

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 10:50
 */

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = this.id,
        description = this.description,
        isCompleted = this.isCompleted,
        priority = this.priority,
        dueDate = this.dueDate,
        workId = this.workId,
        createdAt = this.createdAt
    )
}

fun TaskEntity.toDomain(): Task {
    return Task(
        id = this.id,
        description = this.description,
        isCompleted = this.isCompleted,
        priority = this.priority,
        dueDate = this.dueDate,
        workId = this.workId,
        createdAt = this.createdAt
    )
}
