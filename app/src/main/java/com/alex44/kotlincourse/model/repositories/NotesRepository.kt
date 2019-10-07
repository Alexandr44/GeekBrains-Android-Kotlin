package com.alex44.kotlincourse.model.repositories

import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.providers.RemoteDataProvider

class NotesRepository(private val dataProvider : RemoteDataProvider) {

    fun getNotes() = dataProvider.subscribeToAllNotes()

    fun getNoteById(id : String) = dataProvider.getNoteById(id)

    fun saveNote(note : Note) = dataProvider.saveNote(note)

    fun deleteNote(note : Note) = dataProvider.deleteNote(note)

    fun getCurrentUser() = dataProvider.getCurrentUser()
}