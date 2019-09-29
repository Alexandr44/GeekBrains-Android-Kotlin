package com.alex44.kotlincourse.view.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.model.errors.NoAuthException
import com.alex44.kotlincourse.viewmodel.BaseViewModel
import com.alex44.kotlincourse.viewmodel.states.BaseViewState
import com.firebase.ui.auth.AuthUI

abstract class BaseActivity<T, G : BaseViewState<T>> : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 16884
    }

    abstract val viewModel : BaseViewModel<T, G>
    abstract val layoutResource : Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutResource?.let { setContentView(it) }
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
        when (error) {
            is NoAuthException -> startLogin()
            else -> error.message?.let {
                showError(it)
            }
        }
    }

    private fun startLogin() {
        val providers = listOf (
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setLogo(R.drawable.icon)
                .setTheme(R.style.LoginStyle)
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
            finish()
        }
        else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showError(message : String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}