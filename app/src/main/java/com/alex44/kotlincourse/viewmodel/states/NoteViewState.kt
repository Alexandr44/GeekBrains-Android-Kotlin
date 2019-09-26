package com.alex44.kotlincourse.viewmodel.states

import com.alex44.kotlincourse.model.dtos.NoteDTO

class NoteViewState(note : NoteDTO? = null, error : Throwable? = null) : BaseViewState<NoteDTO?>(note, error)