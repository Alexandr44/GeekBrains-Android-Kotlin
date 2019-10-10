package com.alex44.kotlincourse.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.dtos.User
import com.alex44.kotlincourse.model.errors.NoAuthException
import com.alex44.kotlincourse.model.repositories.NotesRepository
import com.alex44.kotlincourse.viewmodel.MainViewModel
import com.alex44.kotlincourse.viewmodel.NoteViewModel
import com.alex44.kotlincourse.viewmodel.SplashViewModel
import com.alex44.kotlincourse.viewmodel.states.NoteViewState
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockRepo = mock<NotesRepository>()
    private val splashLiveData = MutableLiveData<User>()

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        reset(mockRepo)
        whenever(mockRepo.getCurrentUser()).thenReturn(splashLiveData)
        viewModel = SplashViewModel(mockRepo)
    }

    @Test
    fun `requestUser should return User`() {
        var result: Boolean? = false
        val user = User("Name", "Email", "Phone", "Url")
        viewModel.getViewState().observeForever {
            result = it?.data
        }
        viewModel.requestUser()
        splashLiveData.value = user
        assertTrue(result?:false)
    }

    @Test
    fun `requestUser should return error`() {
        var result: Throwable? = null
        val error = NoAuthException()
        viewModel.getViewState().observeForever {
            result = it?.error
        }
        viewModel.requestUser()
        splashLiveData.value = null
        assertTrue(result != null)
        assertEquals(error::class.java, result!!::class.java)
    }

}