package com.ankurkushwaha.momento.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 10:46
 */
@Entity(tableName = "notes")
data class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String? = null,
    val description: String? = null,
    val date: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false
)
