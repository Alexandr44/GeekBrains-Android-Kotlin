package com.alex44.kotlincourse.viewmodel

import com.alex44.kotlincourse.model.errors.NoAuthException
import com.alex44.kotlincourse.model.repositories.NotesRepository
import kotlinx.coroutines.launch

open class SplashViewModel(private val repo : NotesRepository) : BaseViewModel<Boolean?>() {

    fun requestUser() {
        launch {
            repo.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }
    }

}