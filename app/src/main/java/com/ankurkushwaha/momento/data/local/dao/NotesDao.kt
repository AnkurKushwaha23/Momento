package com.ankurkushwaha.momento.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.ankurkushwaha.momento.data.local.entity.NotesEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 10:52
 */

@Dao
interface NotesDao {

    @Upsert
    suspend fun insertNote(note: NotesEntity)

    @Update
    suspend fun updateNote(note: NotesEntity)

    @Delete
    suspend fun deleteNote(note: NotesEntity)

    @Query("SELECT isPinned FROM notes WHERE id = :noteId")
    suspend fun isNotePinned(noteId: Int): Boolean

    @Query("UPDATE notes SET isPinned = :pinned WHERE id = :noteId")
    suspend fun updatePinnedStatus(noteId: Int, pinned: Boolean)

    @Query("SELECT * FROM notes ORDER BY date DESC")
    fun getAllNotes(): Flow<List<NotesEntity>>

    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): NotesEntity?
}
