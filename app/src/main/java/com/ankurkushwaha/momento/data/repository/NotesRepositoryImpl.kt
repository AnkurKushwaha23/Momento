package com.ankurkushwaha.momento.data.repository

import com.ankurkushwaha.momento.data.local.dao.NotesDao
import com.ankurkushwaha.momento.data.mapper.toDomain
import com.ankurkushwaha.momento.data.mapper.toEntity
import com.ankurkushwaha.momento.domain.model.Notes
import com.ankurkushwaha.momento.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl(
    private val notesDao: NotesDao
) : NotesRepository {
    override suspend fun insertNote(note: Notes) {
        val noteEntity = note.toEntity()
        notesDao.insertNote(noteEntity)
    }

    override suspend fun updateNote(note: Notes) {
        val noteEntity = note.toEntity()
        notesDao.updateNote(noteEntity)
    }

    override suspend fun deleteNote(note: Notes) {
        val noteEntity = note.toEntity()
        notesDao.deleteNote(noteEntity)
    }

    override suspend fun isNotePinned(noteId: Int): Boolean {
        return notesDao.isNotePinned(noteId)
    }

    override suspend fun updatePinnedStatus(noteId: Int, pinned: Boolean) {
        notesDao.updatePinnedStatus(noteId, pinned)
    }

    override suspend fun getNoteById(id: Int): Notes {
        return notesDao.getNoteById(id)!!.toDomain()
    }

    override fun getAllNotes(): Flow<List<Notes>> {
        return notesDao.getAllNotes().map { notes ->
            notes.map { it.toDomain() }
        }
    }

    override fun getAllPinnedNotes(): Flow<List<Notes>> {
        return notesDao.getAllNotes()
            .map { notes ->
                notes.filter { it.isPinned }
                    .map { it.toDomain() }
            }
    }

    override fun getAllUnpinnedNotes(): Flow<List<Notes>> {
        return notesDao.getAllNotes()
            .map { notes ->
                notes.filter { !(it.isPinned) }
                    .map { it.toDomain() }
            }
    }
}