package com.alex44.kotlincourse.viewmodel

import androidx.lifecycle.Observer
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.repositories.NotesRepository
import com.alex44.kotlincourse.viewmodel.states.NoteViewState

class NoteViewModel(private val repo : NotesRepository) : BaseViewModel<Note?, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }

    fun save(note : Note) {
        repo.saveNote(note)
    }

    fun loadNote(id : String) {
        repo.getNoteById(id).observeForever(Observer<NoteResult> {result ->
            if (result == null) return@Observer

            when(result) {
                is NoteResult.Success<*> -> viewStateLiveData.value = NoteViewState(note = result.data as? Note)
                is NoteResult.Error -> viewStateLiveData.value = NoteViewState(error = result.error)
            }
        })
    }

    fun delete(note: Note) {
        repo.deleteNote(note)
    }

}