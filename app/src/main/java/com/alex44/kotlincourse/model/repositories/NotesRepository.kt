package com.alex44.kotlincourse.model.repositories

import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.providers.FireStoreProvider
import com.alex44.kotlincourse.model.providers.RemoteDataProvider

object NotesRepository {

    private val dataProvider : RemoteDataProvider = FireStoreProvider()

    fun getNotes() = dataProvider.subscribeToAllNotes()

    fun getNoteById(id : String) = dataProvider.getNoteById(id)

    fun saveNote(note : Note) = dataProvider.saveNote(note)

    fun deleteNote(note : Note) = dataProvider.deleteNote(note)

    fun getCurrentUser() = dataProvider.getCurrentUser()
}