package com.alex44.kotlincourse.view.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.model.extensions.getColorInt
import com.alex44.kotlincourse.viewmodel.NoteViewModel
import com.alex44.kotlincourse.viewmodel.states.NoteViewState
import kotlinx.android.synthetic.main.activity_note.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.contracts.ExperimentalContracts

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"
        private const val DURATION : Long = 300

        fun start(context: Context, note: Note? = null) {
            val intent = Intent(context, NoteActivity::class.java).apply {
                note?.let {
                    putExtra(EXTRA_NOTE, note.id)
                }
            }
            context.startActivity(intent)
        }
    }

    private var color: Note.Color = Note.Color.WHITE
    private var note : Note? = null
    override val layoutResource: Int = R.layout.activity_note
    override val model : NoteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(note_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let {
            model.loadNote(it)
        } ?: let {
            getString(R.string.new_note_bar_title)
        }
    }

    private fun initBar() {
        supportActionBar?.title = note?.let {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note!!.dateUpdate)
        } ?: getString(R.string.new_note_bar_title)
    }

    private fun initNote() {
        if (note != null) {
            note_title.setText(note!!.title)
            note_text.setText(note!!.text)
            val color = when (note!!.color) {
                Note.Color.RED -> R.color.noteColorRed
                Note.Color.ORANGE -> R.color.noteColorOrange
                Note.Color.YELLOW -> R.color.noteColorYellow
                Note.Color.GREEN -> R.color.noteColorGreen
                Note.Color.BLUE -> R.color.noteColorBlue
                Note.Color.VIOLET -> R.color.noteColorViolet
                Note.Color.PINK -> R.color.noteColorPink
                Note.Color.WHITE -> R.color.noteColorWhite
                Note.Color.BLACK -> R.color.noteColorBlack
            }

            note_toolbar.setBackgroundColor(ContextCompat.getColor(this, color))
        }

    }

    private fun initButtons() {
        button_ok.setOnClickListener {
           saveNote()
        }

        button_cancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initColorPicker() {
        colorPicker.onColorClickListener = { color ->
            this.color = color
            note_toolbar.setBackgroundColor(color.getColorInt(this))
            hor_scroll_view.setBackgroundColor(color.getColorInt(this))
        }
    }

    @ExperimentalContracts
    override fun renderData(data: Note?) {
        this.note = data
        this.color = note?.color ?: Note.Color.WHITE
        initBar()
        initNote()
        initButtons()
        initColorPicker()
    }

    private fun saveNote() {
        if (note_title.text == null || note_title.text?.length ?: 0 <= 0) return
        note = note?.copy(
                title = note_title.text.toString(),
                text = note_text.text.toString(),
                dateUpdate = Date(),
                color = this.color
        ) ?: Note(
                UUID.randomUUID().toString(),
                note_title.text.toString(),
                note_text.text.toString(),
                this.color
        )
        note?.let {
            model.save(it)
        }
        onBackPressed()
    }

    private fun deleteNote() {
        note?.let {
            model.delete(it)
        }
        onBackPressed()
    }

    private fun togglePallete() {
        if (colorPicker.isOpen) {
            colorPicker.close()
            hor_scroll_view.animate().
                    y(-hor_scroll_view.height.toFloat())
                    .setDuration(DURATION)
                    .setListener(null)
                    .start()
            note_layout.animate()
                    .yBy(-hor_scroll_view.height.toFloat())
                    .setDuration(DURATION)
                    .start()
        } else {
            hor_scroll_view.setBackgroundColor(color.getColorInt(this))
            note_layout.animate()
                    .yBy(hor_scroll_view.height.toFloat())
                    .setDuration(DURATION)
                    .start()
            hor_scroll_view.animate().
                    y(hor_scroll_view.height.toFloat())
                    .setDuration(DURATION)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            colorPicker.open()
                        }
                    })
                    .start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?) = menuInflater.inflate(R.menu.note_menu, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        R.id.pallete -> togglePallete().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

}
