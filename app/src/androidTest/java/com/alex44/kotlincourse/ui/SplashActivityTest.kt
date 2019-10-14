package com.alex44.kotlincourse.ui

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.rule.ActivityTestRule
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.view.ui.activities.NoteActivity
import com.alex44.kotlincourse.view.ui.activities.SplashActivity
import com.alex44.kotlincourse.viewmodel.NoteViewModel
import com.alex44.kotlincourse.viewmodel.SplashViewModel
import com.alex44.kotlincourse.viewmodel.states.NoteViewState
import com.alex44.kotlincourse.viewmodel.states.SplashViewState
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashActivityTest {
    @get:Rule
    val activityTestRule = ActivityTestRule(SplashActivity::class.java, true, false)

    private val model: SplashViewModel = Mockito.mock(SplashViewModel::class.java)
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

        doReturn(viewStateLiveData).`when`(model).getViewState()
        doNothing().`when`(model).requestUser()

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