package com.ankurkushwaha.momento.presentation.notes_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ankurkushwaha.momento.domain.model.Notes
import com.ankurkushwaha.momento.domain.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Ankur Kushwaha
 * Created on 2025/04/16 at 11:42
 */

class NotesViewModel(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _pinnedNotes = MutableStateFlow<List<Notes>>(emptyList())
    val pinnedNotes = _pinnedNotes.asStateFlow()

    private val _unpinnedNotes = MutableStateFlow<List<Notes>>(emptyList())
    val unpinnedNotes = _unpinnedNotes.asStateFlow()

    private val _selectedNote = MutableStateFlow<Notes?>(null)
    val selectedNote = _selectedNote.asStateFlow()

    private val _showAddNoteSheet = MutableStateFlow<Boolean>(false)
    val showAddNoteSheet = _showAddNoteSheet.asStateFlow()

    private val _lastDeletedNote = MutableStateFlow<Notes?>(null)
    val lastDeletedNote = _lastDeletedNote.asStateFlow()

    // Add state for showing snackbar
    private val _showUndoSnackbar = MutableStateFlow(false)
    val showUndoSnackbar = _showUndoSnackbar.asStateFlow()

    init {
        getAllPinnedNotes()
        getAllUnPinnedNotes()
    }

    fun showAddNoteBottomSheet() {
        _showAddNoteSheet.value = true
    }

    fun hideAddNoteBottomSheet() {
        _showAddNoteSheet.value = false
        _selectedNote.value = null
    }

    fun insertNote(notes: Notes) {
        viewModelScope.launch {
            notesRepository.insertNote(notes)
            hideAddNoteBottomSheet()
        }
    }

    fun updateNote(notes: Notes) {
        viewModelScope.launch {
            notesRepository.updateNote(notes)
            hideAddNoteBottomSheet()
        }
    }

    // Modified delete function
    fun deleteNote(notes: Notes) {
        viewModelScope.launch {
            // Save the note before deleting
            _lastDeletedNote.value = notes
            notesRepository.deleteNote(notes)
            hideAddNoteBottomSheet()
            // Trigger snackbar
            _showUndoSnackbar.value = true
        }
    }

    // Add function to undo deletion
    fun undoDelete() {
        viewModelScope.launch {
            _lastDeletedNote.value?.let { note ->
                notesRepository.insertNote(note)
                _lastDeletedNote.value = null
            }
        }
    }

    // Reset snackbar state
    fun snackbarShown() {
        _showUndoSnackbar.value = false
    }

    fun updatePinnedStatus(noteId: Int, isPinned: Boolean) {
        viewModelScope.launch {
            notesRepository.updatePinnedStatus(noteId, isPinned)
        }
    }

    fun setSelectedNote(notes: Notes) {
        viewModelScope.launch {
            _selectedNote.value = notes
        }
    }

    fun getNoteById(id: Int) {
        viewModelScope.launch {
            _selectedNote.value = notesRepository.getNoteById(id)
        }
    }

    fun getAllPinnedNotes() {
        viewModelScope.launch {
            notesRepository.getAllPinnedNotes().collect { notes ->
                _pinnedNotes.value = notes
            }
        }
    }

    fun getAllUnPinnedNotes() {
        viewModelScope.launch {
            notesRepository.getAllUnpinnedNotes().collect { notes ->
                _unpinnedNotes.value = notes
            }
        }
    }
}

//    fun togglePinnedStatus(noteId: Int) {
//        viewModelScope.launch {
//            val isPinned = notesRepository.isNotePinned(noteId)
//            notesRepository.updatePinnedStatus(noteId, !isPinned)
//        }
//    }