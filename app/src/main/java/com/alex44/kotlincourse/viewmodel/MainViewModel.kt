package com.alex44.kotlincourse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.NoteDTO
import com.alex44.kotlincourse.model.repositories.NotesRepository
import com.alex44.kotlincourse.viewmodel.states.MainViewState

class MainViewModel : BaseViewModel<List<NoteDTO>?, MainViewState>() {

    private val repositoryNotes = NotesRepository.getNotes()

    private val observer = Observer<NoteResult> {noteResult ->
        if (noteResult == null) return@Observer

        when (noteResult) {
            is NoteResult.Success<*> -> {
                viewStateLiveData.value = MainViewState(notes = noteResult.data as? List<NoteDTO>)
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