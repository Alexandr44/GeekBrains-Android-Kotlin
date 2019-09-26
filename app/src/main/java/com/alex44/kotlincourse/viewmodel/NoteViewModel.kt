package com.alex44.kotlincourse.viewmodel

import androidx.lifecycle.Observer
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.NoteDTO
import com.alex44.kotlincourse.model.repositories.NotesRepository
import com.alex44.kotlincourse.viewmodel.states.NoteViewState

class NoteViewModel : BaseViewModel<NoteDTO?, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    fun save(note : NoteDTO) {
        NotesRepository.saveNote(note)
    }

    fun loadNote(id : String) {
        NotesRepository.getNoteById(id).observeForever(Observer<NoteResult> {result ->
            if (result == null) return@Observer

            when(result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(note = result.data as? NoteDTO)
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
        })
    }

    fun delete(note: NoteDTO) {
        NotesRepository.deleteNote(note)
    }

}