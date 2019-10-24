package com.alex44.kotlincourse.viewmodel

import androidx.annotation.VisibleForTesting
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.repositories.NotesRepository
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

open class MainViewModel(private val repo : NotesRepository) : BaseViewModel<List<Note>?>() {

    public val notesChannel = repo.getNotes()

    init {
        launch {
            notesChannel.consumeEach {result ->
                when (result) {
                    is NoteResult.Success<*> -> setData(result.data as? List<Note>)
                    is NoteResult.Error -> setError(result.error)
                }
            }
        }
    }

    @VisibleForTesting
    public override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }

}