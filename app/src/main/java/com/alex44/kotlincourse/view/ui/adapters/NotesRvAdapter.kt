package com.alex44.kotlincourse.view.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.model.dtos.NoteDTO
import kotlinx.android.synthetic.main.note_item.view.*

class NotesRvAdapter(val onItemClick: ((NoteDTO) -> Unit)? = null) : RecyclerView.Adapter<NotesRvAdapter.ViewHolder>() {

    var notes: List<NoteDTO> = listOf()
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

        fun bind(note: NoteDTO) = with(itemView) {
            item_title.text = note.title
            item_text.text = note.text

            val color = when(note.color) {
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

            itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, color))
            itemView.setOnClickListener {
                onItemClick?.invoke(note)
            }
        }
    }

}