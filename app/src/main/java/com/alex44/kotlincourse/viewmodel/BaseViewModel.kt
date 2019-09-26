package com.alex44.kotlincourse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex44.kotlincourse.viewmodel.states.BaseViewState

open class BaseViewModel<T, G : BaseViewState<T>> : ViewModel() {

    open val viewStateLiveData = MutableLiveData<G>()

    open fun getViewState() : LiveData<G> = viewStateLiveData

}