package com.alex44.kotlincourse.view.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.model.dtos.NoteDTO
import kotlinx.android.synthetic.main.note_item.view.*

class NotesRvAdapter : RecyclerView.Adapter<NotesRvAdapter.ViewHolder>() {

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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: NoteDTO) = with(itemView) {
            item_title.text = note.title
            item_text.text = note.text
            itemView.setBackgroundColor(note.color)
        }
    }

}