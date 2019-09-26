package com.alex44.kotlincourse.model.providers

import androidx.lifecycle.LiveData
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.NoteDTO

interface RemoteDataProvider {
    fun subscribeToAllNotes() : LiveData<NoteResult>
    fun getNoteById(id : String) : LiveData<NoteResult>
    fun saveNote(note : NoteDTO) : LiveData<NoteResult>
    fun deleteNote(note : NoteDTO) : LiveData<NoteResult>
}