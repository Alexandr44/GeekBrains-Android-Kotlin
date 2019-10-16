package com.alex44.kotlincourse.ui

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.alex44.kotlincourse.view.ui.activities.SplashActivity
import com.alex44.kotlincourse.viewmodel.SplashViewModel
import com.alex44.kotlincourse.viewmodel.states.SplashViewState
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashActivityTest {
    @get:Rule
    val activityTestRule = IntentsTestRule(SplashActivity::class.java, true, false)

    private val model: SplashViewModel = mockk(relaxed = true)
    private val viewStateLiveData = MutableLiveData<SplashViewState>()

    @Before
    fun setup() {
        loadKoinModules(
                listOf(
                        module {
                            viewModel(override = true) { model }
                        }
                )
        )

        every { model.getViewState() } returns viewStateLiveData
        every { model.requestUser() } just runs

        Intent().let {
            activityTestRule.launchActivity(it)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun test() {

    }

}