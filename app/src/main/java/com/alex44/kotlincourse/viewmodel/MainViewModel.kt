package com.alex44.kotlincourse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alex44.kotlincourse.model.MainModel

class MainViewModel : ViewModel() {

    private val viewStateLiveData = MutableLiveData<String>()

    private val mainModel = MainModel(0)

    var count = 0

    init {
        viewStateLiveData.value = "Hello World!!!"
    }

    fun viewState() : LiveData<String> = viewStateLiveData

    fun plus_btn_click() {
        count++
        mainModel.update(count, "Plus")
        viewStateLiveData.value = "$count | $mainModel"
    }

    fun minus_btn_click() {
        count--
        mainModel.update(count, "Minus")
        viewStateLiveData.value = "$count | $mainModel"
    }

    fun longClick() : Boolean {
        count = 0
        mainModel.update(count, "Clear")
        viewStateLiveData.value = "$count | $mainModel"
        return true
    }


}