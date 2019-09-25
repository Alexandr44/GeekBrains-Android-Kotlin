package com.alex44.kotlincourse.model.dtos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class NoteDTO (
    val id: String,
    val title: String,
    val text: String,
    val color: Color = Color.WHITE,
    val dateUpdate : Date = Date()
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteDTO

        if (id != other.id) return false
        return true
    }

    enum class Color {
        RED,
        ORANGE,
        YELLOW,
        GREEN,
        BLUE,
        VIOLET,
        PINK,
        WHITE,
        BLACK
    }

}