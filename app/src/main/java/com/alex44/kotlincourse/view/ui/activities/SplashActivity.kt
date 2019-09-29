package com.alex44.kotlincourse.view.ui.activities

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.viewmodel.SplashViewModel
import com.alex44.kotlincourse.viewmodel.states.SplashViewState

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    companion object {
        private const val START_DELAY = 1000L
    }

    override val viewModel: SplashViewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    override val layoutResource: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashTheme)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let { startMainActivity() }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }

}
