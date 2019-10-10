package com.alex44.kotlincourse.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.repositories.NotesRepository
import com.alex44.kotlincourse.viewmodel.MainViewModel
import com.alex44.kotlincourse.viewmodel.NoteViewModel
import com.alex44.kotlincourse.viewmodel.states.NoteViewState
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepo = mock<NotesRepository>()
    private val notesLiveData = MutableLiveData<NoteResult>()

    private val testNote = Note("id1", "Title1", "Text1")

    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup() {
        reset(mockRepo)
        whenever(mockRepo.saveNote(testNote)).thenReturn(notesLiveData)
        whenever(mockRepo.getNoteById(testNote.id)).thenReturn(notesLiveData)
        whenever(mockRepo.deleteNote(testNote)).thenReturn(notesLiveData)
        viewModel = NoteViewModel(mockRepo)
    }

    @Test
    fun `should call save once`() {
        viewModel.save(testNote)
        verify(mockRepo, times(1)).saveNote(testNote)
    }

    @Test
    fun `loadNote should return Note`() {
        var result: Note? = null
        viewModel.getViewState().observeForever {
            result = it?.data
        }
        viewModel.loadNote(testNote.id)
        notesLiveData.value = NoteResult.Success(result)
        assertEquals(testNote, result)
    }

//    @Test
//    fun `should return notes`() {
//        var result: List<Note>? = null
//        val testData = listOf(Note("id1"), Note("id2"))
//        viewModel.getViewState().observeForever{
//            result = it?.data
//        }
//        notesLiveData.value = NoteResult.Success(testData)
//        assertEquals(testData, result)
//    }



/*
    @Test
    fun `should call delete once`() {
        notesLiveData.value = NoteResult.Success(testNote)
        var result: MutableLiveData<NoteResult>? = null

        var noteRes: Note? = null
        viewModel.getViewState().observeForever{
            noteRes = it?.data
        }

        viewModel.delete(testNote)
//        verify(mockRepo, times(1)).deleteNote(testNote)
        assertEquals(testNote, (result as? NoteResult.Success<NoteResult>)?.data)
    }
*/
}