package com.alex44.kotlincourse.viewmodel.states

import com.alex44.kotlincourse.model.dtos.NoteDTO

class MainViewState (val notes : List<NoteDTO>? = null, error : Throwable? = null) : BaseViewState<List<NoteDTO>?>(notes, error)