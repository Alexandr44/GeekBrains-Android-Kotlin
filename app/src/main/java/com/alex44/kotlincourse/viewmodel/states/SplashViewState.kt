package com.alex44.kotlincourse.viewmodel.states

class SplashViewState (authenticated : Boolean? = null, error : Throwable? = null) : BaseViewState<Boolean?>(authenticated, error)