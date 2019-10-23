package com.alex44.kotlincourse.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.User
import com.alex44.kotlincourse.model.errors.NoAuthException
import com.alex44.kotlincourse.model.repositories.NotesRepository
import com.alex44.kotlincourse.viewmodel.SplashViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepo = mock<NotesRepository>()

    private lateinit var viewModel: SplashViewModel

    private val testUser = User("name1")

    private val channel = Channel<NoteResult>(Channel.CONFLATED)

    @Before
    fun setup() {
        reset(mockRepo)
        whenever(mockRepo.getNotes()).thenReturn(channel)
        viewModel = SplashViewModel(mockRepo)
    }

    @Test
    fun `requestUser should return true`() {
        runBlocking {
            whenever(mockRepo.getCurrentUser()).thenReturn(testUser)

            val deferred = async {
                viewModel.getViewState().receive()
            }

            viewModel.requestUser()

            val result = deferred.await()
            assertTrue(result ?: false)
        }
    }

    @Test
    fun `requestUser should return error`() {
        runBlocking {
            whenever(mockRepo.getCurrentUser()).thenReturn(null)

            val deferred = async {
                viewModel.getErrorChannel().receive()
            }

            viewModel.requestUser()

            val result = deferred.await()
            assertEquals(NoAuthException::class.java, result::class.java)
        }
    }

}