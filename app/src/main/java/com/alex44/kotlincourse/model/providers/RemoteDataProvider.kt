package com.alex44.kotlincourse.model.providers

import androidx.lifecycle.LiveData
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.dtos.User

interface RemoteDataProvider {
    fun subscribeToAllNotes() : LiveData<NoteResult>
    fun getNoteById(id : String) : LiveData<NoteResult>
    fun saveNote(note : Note) : LiveData<NoteResult>
    fun deleteNote(note : Note) : LiveData<NoteResult>
    fun getCurrentUser() : LiveData<User?>
}