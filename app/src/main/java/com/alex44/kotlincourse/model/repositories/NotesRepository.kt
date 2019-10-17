package com.alex44.kotlincourse.model.repositories

import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.providers.RemoteDataProvider

class NotesRepository(private val dataProvider : RemoteDataProvider) {

    fun getNotes() = dataProvider.subscribeToAllNotes()
    suspend fun getNoteById(id : String) = dataProvider.getNoteById(id)
    suspend fun saveNote(note : Note) = dataProvider.saveNote(note)
    suspend fun deleteNote(note : Note) = dataProvider.deleteNote(note)
    suspend fun getCurrentUser() = dataProvider.getCurrentUser()
}