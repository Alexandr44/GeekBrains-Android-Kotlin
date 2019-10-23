package com.alex44.kotlincourse.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.repositories.NotesRepository
import com.alex44.kotlincourse.viewmodel.MainViewModel
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepo = mock<NotesRepository>()

    private lateinit var viewModel: MainViewModel

    private val testNote = Note("id1")
    private val testError = Throwable("error")

    private val channel = Channel<NoteResult>(Channel.CONFLATED)
    @Before
    fun setup() {
        reset(mockRepo)
        whenever(mockRepo.getNotes()).thenReturn(channel)
        viewModel = MainViewModel(mockRepo)
    }

    @Test
    fun `should call getNotes once`() {
        verify(mockRepo, times(1)).getNotes()
    }

    @Test
    fun `should return notes`() {
        runBlocking {
            val deferred = async {
                viewModel.getViewState().receive()
            }

            channel.send(NoteResult.Success(listOf(testNote)))
            val result = deferred.await()
            assertEquals(listOf(testNote), result)
        }
    }

    @Test
    fun `should return error`() {
        runBlocking {
            val deferred = async {
                viewModel.getErrorChannel().receive()
            }

            channel.send(NoteResult.Error(testError))
            val result = deferred.await()
            assertEquals(testError, result)
        }
    }

}