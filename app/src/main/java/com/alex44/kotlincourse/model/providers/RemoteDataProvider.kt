package com.alex44.kotlincourse.model.providers

import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.dtos.User
import kotlinx.coroutines.channels.ReceiveChannel

interface RemoteDataProvider {
    fun subscribeToAllNotes() : ReceiveChannel<NoteResult>
    suspend fun getNoteById(id : String) : Note
    suspend fun saveNote(note : Note) : Note
    suspend fun deleteNote(note : Note) : Note
    suspend fun getCurrentUser() : User?
}