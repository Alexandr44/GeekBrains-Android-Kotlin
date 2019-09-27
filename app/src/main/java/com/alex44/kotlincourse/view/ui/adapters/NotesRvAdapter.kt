package com.alex44.kotlincourse.view.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.model.dtos.Note
import kotlinx.android.synthetic.main.note_item.view.*

class NotesRvAdapter(val onItemClick: ((Note) -> Unit)? = null) : RecyclerView.Adapter<NotesRvAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false) )
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note) = with(itemView) {
            item_title.text = note.title
            item_text.text = note.text

            val color = when(note.color) {
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

            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, color))
            itemView.setOnClickListener {
                onItemClick?.invoke(note)
            }
        }
    }

}