package com.alex44.kotlincourse.view.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.model.dtos.NoteDTO
import com.alex44.kotlincourse.viewmodel.NoteViewModel
import com.alex44.kotlincourse.viewmodel.states.NoteViewState
import kotlinx.android.synthetic.main.activity_note.*
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<NoteDTO?, NoteViewState>() {

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, note: NoteDTO? = null) {
            val intent = Intent(context, NoteActivity::class.java).apply {
                note?.let {
                    putExtra(EXTRA_NOTE, note.id)
                }
            }
            context.startActivity(intent)
        }
    }

    private var note : NoteDTO? = null
    override val layoutResource: Int = R.layout.activity_note
    override val viewModel : NoteViewModel by lazy {
        ViewModelProviders.of(this).get(NoteViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(note_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let {
            viewModel.loadNote(it)
        } ?: let {
            getString(R.string.new_note_bar_title)
        }
    }

    private fun initBar() {
        supportActionBar?.title = note?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note!!.dateUpdate)
        } ?: getString(R.string.new_note_bar_title)
        if (note != null) {
            button_delete.visibility = VISIBLE
        }
    }

    private fun initNote() {
        if (note != null) {
            note_title.setText(note!!.title)
            note_text.setText(note!!.text)
            val color = when (note!!.color) {
                NoteDTO.Color.RED -> R.color.noteColorRed
                NoteDTO.Color.ORANGE -> R.color.noteColorOrange
                NoteDTO.Color.YELLOW -> R.color.noteColorYellow
                NoteDTO.Color.GREEN -> R.color.noteColorGreen
                NoteDTO.Color.BLUE -> R.color.noteColorBlue
                NoteDTO.Color.VIOLET -> R.color.noteColorViolet
                NoteDTO.Color.PINK -> R.color.noteColorPink
                NoteDTO.Color.WHITE -> R.color.noteColorWhite
                NoteDTO.Color.BLACK -> R.color.noteColorBlack
            }

            note_toolbar.setBackgroundColor(ContextCompat.getColor(this, color))
        }

    }

    private fun initButtons() {
        button_ok.setOnClickListener {
           if (note_title.text == null || note_title.text?.length ?: 0 <= 0) return@setOnClickListener
            note = note?.copy(
                    title = note_title.text.toString(),
                    text = note_text.text.toString(),
                    dateUpdate = Date()
            ) ?: NoteDTO(
                    UUID.randomUUID().toString(),
                    note_title.text.toString(),
                    note_text.text.toString()
            )
            note?.let {
                viewModel.save(it)
            }
            onBackPressed()
        }

        button_cancel.setOnClickListener {
            onBackPressed()
        }

        button_delete.setOnClickListener {
            note?.let {
                viewModel.delete(it)
            }
            onBackPressed()
        }
    }

    override fun renderData(data: NoteDTO?) {
        this.note = data
        initBar()
        initNote()
        initButtons()
    }

}
