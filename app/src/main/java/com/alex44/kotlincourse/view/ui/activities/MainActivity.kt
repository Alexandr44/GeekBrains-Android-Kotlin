package com.alex44.kotlincourse.view.ui.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.model.dtos.NoteDTO
import com.alex44.kotlincourse.view.ui.adapters.NotesRvAdapter
import com.alex44.kotlincourse.viewmodel.MainViewModel
import com.alex44.kotlincourse.viewmodel.states.MainViewState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<List<NoteDTO>?, MainViewState>() {

    override val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override val layoutResource : Int = R.layout.activity_main

    lateinit var adapter: NotesRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(main_toolbar)
        initRv()
        initFab()
    }

    private fun initRv() {
        rv_notes.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRvAdapter{
            NoteActivity.start(this, it)
        }
        rv_notes.adapter = adapter
    }

    private fun initFab() {
        fab.setOnClickListener {
            NoteActivity.start(this)
        }
    }

    override fun renderData(data: List<NoteDTO>?) {
        data?.let {list ->
            adapter.notes = list
        }
    }

}
