package com.alex44.kotlincourse.viewmodel

import com.alex44.kotlincourse.model.errors.NoAuthException
import com.alex44.kotlincourse.model.repositories.NotesRepository
import com.alex44.kotlincourse.viewmodel.states.SplashViewState

open class SplashViewModel(private val repo : NotesRepository) : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        repo.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(authenticated = true)
            }
            else {
                SplashViewState(error = NoAuthException())
            }
        }
    }

}