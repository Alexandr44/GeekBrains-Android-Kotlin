package com.alex44.kotlincourse.viewmodel.states

import com.alex44.kotlincourse.model.dtos.Note

class MainViewState (val notes : List<Note>? = null, error : Throwable? = null) : BaseViewState<List<Note>?>(notes, error)