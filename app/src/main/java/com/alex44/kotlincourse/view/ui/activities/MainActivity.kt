package com.alex44.kotlincourse.view.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.view.ui.adapters.NotesRvAdapter
import com.alex44.kotlincourse.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : MainViewModel
    lateinit var adapter: NotesRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        initViewModel()
        initRv()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.viewState().observe(this, Observer {viewState ->
            viewState?.let {//Тут можно тоже написать 'типа vs ->'? тогда нужно будет поменять it. Эти нубские комменты потом нужно будет удалить
                adapter.notes = it.notes
            }
        })
    }

    private fun initRv() {
        rv_notes.layoutManager = GridLayoutManager(this, 3)
        adapter = NotesRvAdapter()
        rv_notes.adapter = adapter
    }

}
