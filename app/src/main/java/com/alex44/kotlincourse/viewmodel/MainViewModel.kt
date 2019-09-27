package com.alex44.kotlincourse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.repositories.NotesRepository
import com.alex44.kotlincourse.viewmodel.states.MainViewState

class MainViewModel : BaseViewModel<List<Note>?, MainViewState>() {

    private val repositoryNotes = NotesRepository.getNotes()

    private val observer = Observer<NoteResult> {noteResult ->
        if (noteResult == null) return@Observer

        when (noteResult) {
            is NoteResult.Success<*> -> {
                viewStateLiveData.value = MainViewState(notes = noteResult.data as? List<Note>)
            }
            is NoteResult.Error -> {
                viewStateLiveData.value = MainViewState(error = noteResult.error)
            }
        }
    }

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(observer)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(observer)
    }

    fun viewState() : LiveData<MainViewState> = viewStateLiveData

}