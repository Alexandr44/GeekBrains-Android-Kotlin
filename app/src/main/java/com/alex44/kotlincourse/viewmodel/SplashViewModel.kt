package com.alex44.kotlincourse.viewmodel

import com.alex44.kotlincourse.model.errors.NoAuthException
import com.alex44.kotlincourse.model.repositories.NotesRepository
import com.alex44.kotlincourse.viewmodel.states.SplashViewState

class SplashViewModel : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        NotesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(authenticated = true)
            }
            else {
                SplashViewState(error = NoAuthException())
            }
        }
    }

}