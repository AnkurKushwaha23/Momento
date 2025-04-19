package com.ankurkushwaha.momento.data.mapper

import com.ankurkushwaha.momento.data.local.entity.NotesEntity
import com.ankurkushwaha.momento.domain.model.Notes

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 10:51
 */

fun Notes.toEntity(): NotesEntity {
    return NotesEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        date = this.timestamp,
        isPinned = this.isPinned
    )
}

fun NotesEntity.toDomain(): Notes {
    return Notes(
        id = this.id,
        title = this.title,
        description = this.description,
        timestamp = this.date,
        isPinned = this.isPinned
    )
}
