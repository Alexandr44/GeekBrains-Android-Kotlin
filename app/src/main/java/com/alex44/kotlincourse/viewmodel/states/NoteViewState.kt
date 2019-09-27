package com.alex44.kotlincourse.viewmodel.states

import com.alex44.kotlincourse.model.dtos.Note

class NoteViewState(note : Note? = null, error : Throwable? = null) : BaseViewState<Note?>(note, error)