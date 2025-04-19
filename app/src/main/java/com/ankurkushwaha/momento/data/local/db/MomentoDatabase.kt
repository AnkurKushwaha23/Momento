package com.ankurkushwaha.momento.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ankurkushwaha.momento.data.local.dao.NotesDao
import com.ankurkushwaha.momento.data.local.dao.TaskDao
import com.ankurkushwaha.momento.data.local.entity.NotesEntity
import com.ankurkushwaha.momento.data.local.entity.PriorityConverter
import com.ankurkushwaha.momento.data.local.entity.TaskEntity

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 10:58
 */

@Database(
    entities = [NotesEntity::class, TaskEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PriorityConverter::class)
abstract class MomentoDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun taskDao(): TaskDao
}