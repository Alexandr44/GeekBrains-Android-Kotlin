package com.alex44.kotlincourse.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.repositories.NotesRepository
import com.alex44.kotlincourse.viewmodel.NoteViewModel
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepo = mock<NotesRepository>()

    private val testNote = Note("id1", "Title1", "Text1")
    private val testError = RuntimeException("error")

    private lateinit var viewModel: NoteViewModel

    private val channel = Channel<NoteResult>(Channel.CONFLATED)

    @Before
    fun setup() {
        reset(mockRepo)
        whenever(mockRepo.getNotes()).thenReturn(channel)
        viewModel = NoteViewModel(mockRepo)
    }

    @Test
    fun `should call save once`() {
        runBlocking {
            viewModel.save(testNote)
            delay(100)
            verify(mockRepo, times(1)).saveNote(testNote)
        }
    }

    @Test
    fun `save should return Note`() {
        runBlocking {
            whenever(mockRepo.getNoteById(testNote.id)).thenReturn(testNote)
            val deferred = async {
                viewModel.getViewState().receive()
            }

            viewModel.save(testNote)

            val result = deferred.await()
            assertEquals(testNote, result)
        }
    }

    @Test
    fun `loadNote should return Note`() {
        runBlocking {
            whenever(mockRepo.getNoteById(testNote.id)).thenReturn(testNote)
            val deferred = async {
                viewModel.getViewState().receive()
            }

            viewModel.loadNote(testNote.id)

            val result = deferred.await()
            assertEquals(testNote, result)
        }
    }

    @Test
    fun `loadNote should return error`() {
        runBlocking {
            whenever(mockRepo.getNoteById(testNote.id)).thenThrow(testError)
            val deferred = async {
                viewModel.getErrorChannel().receive()
            }

            viewModel.loadNote(testNote.id)

            val result = deferred.await()
            assertEquals(testError, result)
        }
    }

    @Test
    fun `should call delete once`() {
        runBlocking {
            viewModel.delete(testNote)
            delay(100)
            verify(mockRepo, times(1)).deleteNote(testNote)
        }
    }

    @Test
    fun `delete should return Note`() {
        runBlocking {
            whenever(mockRepo.deleteNote(testNote)).thenReturn(testNote)
            val deferred = async {
                viewModel.getViewState().receive()
            }

            viewModel.delete(testNote)

            val result = deferred.await()
            assertEquals(testNote, result)
        }
    }

    @Test
    fun `delete should return error`() {
        runBlocking {
            whenever(mockRepo.deleteNote(testNote)).thenThrow(testError)
            val deferred = async {
                viewModel.getErrorChannel().receive()
            }

            viewModel.delete(testNote)

            val result = deferred.await()
            assertEquals(testError, result)
        }
    }

}