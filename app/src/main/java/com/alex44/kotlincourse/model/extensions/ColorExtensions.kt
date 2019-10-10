package com.alex44.kotlincourse.model.extensions

import android.content.Context
import androidx.core.content.ContextCompat
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.model.dtos.Note

fun Note.Color.getColorInt(context: Context) = ContextCompat.getColor(
        context, getColorRes()
)

fun Note.Color.getColorRes(): Int = when (this) {
    Note.Color.WHITE -> R.color.noteColorWhite
    Note.Color.YELLOW -> R.color.noteColorYellow
    Note.Color.GREEN -> R.color.noteColorGreen
    Note.Color.BLUE -> R.color.noteColorBlue
    Note.Color.RED -> R.color.noteColorRed
    Note.Color.VIOLET -> R.color.noteColorViolet
    Note.Color.PINK -> R.color.noteColorPink
    Note.Color.ORANGE -> R.color.noteColorOrange
    Note.Color.BLACK -> R.color.noteColorBlack
}