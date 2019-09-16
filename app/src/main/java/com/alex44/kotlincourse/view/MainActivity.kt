package com.alex44.kotlincourse.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViewModel()
        initButtons()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.viewState().observe(this, Observer { str ->
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initButtons() {
        plus_btn.setOnClickListener{ viewModel.plus_btn_click() }
        minus_btn.setOnClickListener{ viewModel.minus_btn_click() }
        plus_btn.setOnLongClickListener { viewModel.longClick() }
        minus_btn.setOnLongClickListener { viewModel.longClick() }
    }
}
