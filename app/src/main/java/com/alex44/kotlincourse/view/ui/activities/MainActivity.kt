package com.alex44.kotlincourse.view.ui.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.view.ui.adapters.NotesRvAdapter
import com.alex44.kotlincourse.viewmodel.MainViewModel
import com.alex44.kotlincourse.viewmodel.states.MainViewState
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    companion object {
        fun start(context : Context) = Intent(context, MainActivity::class.java).run {
            context.startActivity(this)
        }
    }

    override val model: MainViewModel by viewModel()

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

    override fun renderData(data: List<Note>?) {
        val sortedData = data?.sortedWith(compareBy { it.dateUpdate })
        sortedData?.let {list ->
            adapter.notes = list
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = MenuInflater(this).inflate(R.menu.main_menu, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.logout -> showLogoutDialog().let { true }
        else -> false
    }

    private fun showLogoutDialog(){
        alert {
            title = "Выход из учетной записи"
            message = "Вы уверены, что хотите разлогиниться"
            positiveButton("Да") { onLogout() }
            negativeButton("Нет", DialogInterface::dismiss)
        }.show()
    }

    fun onLogout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
            }
    }
}
