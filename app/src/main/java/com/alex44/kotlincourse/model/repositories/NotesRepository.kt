package com.alex44.kotlincourse.model.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alex44.kotlincourse.model.dtos.NoteDTO
import java.util.*

object NotesRepository {

    private val notesLiveData = MutableLiveData<List<NoteDTO>>()

    private val notes = mutableListOf(
            NoteDTO(
                    UUID.randomUUID().toString(),
                    "Первая заметка",
                    "Текст первой заметки. Какой-то очень важный или не очень текст, напоминание, или ещё что-то.",
                    NoteDTO.Color.YELLOW
            ),
            NoteDTO(
                    UUID.randomUUID().toString(),
                    "Вторая заметка",
                    "Текст второй заметки. Какой-то очень важный или не очень текст, напоминание, или ещё что-то.",
                    NoteDTO.Color.BLUE
            ),
            NoteDTO(
                    UUID.randomUUID().toString(),
                    "Третья заметка",
                    "Текст третьей заметки. Какой-то очень важный или не очень текст, напоминание, или ещё что-то.",
                    NoteDTO.Color.GREEN
            ),
            NoteDTO(
                    UUID.randomUUID().toString(),
                    "Четвертая заметка",
                    "Текст четвертой заметки. Какой-то очень важный или не очень текст, напоминание, или ещё что-то.",
                    NoteDTO.Color.RED
            ),
            NoteDTO(
                    UUID.randomUUID().toString(),
                    "Пятая заметка",
                    "Текст пятой заметки. Какой-то очень важный или не очень текст, напоминание, или ещё что-то.",
                    NoteDTO.Color.VIOLET
            ),
            NoteDTO(
                    UUID.randomUUID().toString(),
                    "Шестая заметка",
                    "Текст шестой заметки. Какой-то очень важный или не очень текст, напоминание, или ещё что-то.",
                    NoteDTO.Color.ORANGE
            ),
            NoteDTO(
                    UUID.randomUUID().toString(),
                    "Седьмая заметка",
                    "Текст седьмой заметки. Какой-то очень важный или не очень текст, напоминание, или ещё что-то.",
                    NoteDTO.Color.PINK
            ),
            NoteDTO(
                    UUID.randomUUID().toString(),
                    "Восьмая заметка",
                    "Текст восьмой заметки. Какой-то очень важный или не очень текст, напоминание, или ещё что-то.",
                    NoteDTO.Color.WHITE
            ),
            NoteDTO(
                    UUID.randomUUID().toString(),
                    "Девятая заметка",
                    "Текст девятой заметки. Какой-то очень важный или не очень текст, напоминание, или ещё что-то.",
                    NoteDTO.Color.BLACK
            ),
            NoteDTO(
                    UUID.randomUUID().toString(),
                    "Десятая заметка",
                    "Текст десятой заметки. Какой-то очень важный или не очень текст, напоминание, или ещё что-то.",
                    NoteDTO.Color.ORANGE
            )
    )

    init {
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<NoteDTO>> {
        return notesLiveData
    }

    fun saveNote(note : NoteDTO) {
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note : NoteDTO) {
        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes[i] = note
                return
            }
        }
        notes.add(note)
    }

    fun deleteNote(note: NoteDTO) {
        for (i in 0 until notes.size) {
            if (notes[i] == note) {
                notes.removeAt(i)
                break
            }
        }
        notesLiveData.value = notes
    }

}