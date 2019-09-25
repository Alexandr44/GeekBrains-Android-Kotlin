package com.alex44.kotlincourse.viewmodel

import androidx.lifecycle.ViewModel
import com.alex44.kotlincourse.model.dtos.NoteDTO
import com.alex44.kotlincourse.model.repositories.NotesRepository

class NoteViewModel : ViewModel()  {

    fun save(note : NoteDTO) {
        NotesRepository.saveNote(note)
    }

    fun delete(note: NoteDTO) {
        NotesRepository.deleteNote(note)
    }

}