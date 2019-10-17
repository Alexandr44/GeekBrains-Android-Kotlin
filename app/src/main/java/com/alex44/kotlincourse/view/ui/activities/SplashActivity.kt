package com.alex44.kotlincourse.view.ui.activities

import android.os.Bundle
import android.os.Handler
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.viewmodel.SplashViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<Boolean?>() {

    companion object {
        private const val START_DELAY = 1000L
    }

    override val model: SplashViewModel by viewModel()

    override val layoutResource: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.SplashTheme)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ model.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let { startMainActivity() }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }

}
