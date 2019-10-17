package com.alex44.kotlincourse.view.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.model.errors.NoAuthException
import com.alex44.kotlincourse.viewmodel.BaseViewModel
import com.firebase.ui.auth.AuthUI
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<G> : AppCompatActivity(), CoroutineScope {

    companion object {
        private const val RC_SIGN_IN = 16884
    }

    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Main + Job()
    }

    private lateinit var dataJob: Job
    private lateinit var errorJob: Job

    abstract val model : BaseViewModel<G>
    abstract val layoutResource : Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutResource?.let { setContentView(it) }
    }

    override fun onStart() {
        super.onStart()

        dataJob = launch {
            model.getViewState().consumeEach {
                renderData(it)
            }
        }

        errorJob = launch {
            model.getErrorChannel().consumeEach {
                renderError(it)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        dataJob.cancel()
        errorJob.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }

    abstract fun renderData(data: G)

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