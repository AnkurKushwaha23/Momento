package com.ankurkushwaha.momento.domain.repository

import com.ankurkushwaha.momento.domain.model.Notes
import kotlinx.coroutines.flow.Flow

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/15 at 11:07
 */

interface NotesRepository {
    suspend fun insertNote(note: Notes)
    suspend fun updateNote(note: Notes)
    suspend fun deleteNote(note: Notes)
    suspend fun isNotePinned(noteId: Int): Boolean
    suspend fun updatePinnedStatus(noteId: Int, pinned: Boolean)
    suspend fun getNoteById(id: Int): Notes
    fun getAllNotes(): Flow<List<Notes>>
    fun getAllPinnedNotes(): Flow<List<Notes>>
    fun getAllUnpinnedNotes(): Flow<List<Notes>>
}