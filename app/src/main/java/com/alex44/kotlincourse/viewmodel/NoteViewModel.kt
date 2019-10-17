package com.alex44.kotlincourse.viewmodel

import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.repositories.NotesRepository
import kotlinx.coroutines.launch

open class NoteViewModel(private val repo : NotesRepository) : BaseViewModel<Note?>() {

    fun save(note : Note) {
        setData(note)
        launch {
            note.let { repo.saveNote(it) }
        }
    }

    fun loadNote(id : String) {
        launch {
            try {
                setData(repo.getNoteById(id))
            }
            catch (e: Throwable) {
                setError(e)
            }
        }
    }

    fun delete(note: Note) {
        launch {
            try {
                note.let { setData(repo.deleteNote(it)) }
            } catch (e: Throwable){
                setError(e)
            }
        }
    }

}