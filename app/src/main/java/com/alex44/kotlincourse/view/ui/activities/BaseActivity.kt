package com.alex44.kotlincourse.view.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alex44.kotlincourse.viewmodel.BaseViewModel
import com.alex44.kotlincourse.viewmodel.states.BaseViewState

abstract class BaseActivity<T, G : BaseViewState<T>> : AppCompatActivity() {

    abstract val viewModel : BaseViewModel<T, G>
    abstract val layoutResource : Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)
        viewModel.getViewState().observe(this, Observer<G> {state->
            if (state == null) return@Observer
            if (state.error != null) {
                renderError(state.error)
                return@Observer
            }
            renderData(state.data)
        })
    }

    abstract fun renderData(data: T)

    private fun renderError(error: Throwable) {
        error.message?.let {
            showError(it)
        }
    }

    private fun showError(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}