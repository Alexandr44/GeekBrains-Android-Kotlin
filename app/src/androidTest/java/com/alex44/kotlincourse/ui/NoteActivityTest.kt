package com.alex44.kotlincourse.ui

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.view.ui.activities.NoteActivity
import com.alex44.kotlincourse.viewmodel.NoteViewModel
import com.alex44.kotlincourse.viewmodel.states.NoteViewState
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn

class NoteActivityTest {
    @get:Rule
    val activityTestRule = IntentsTestRule(NoteActivity::class.java, true, false)

    private val model: NoteViewModel = Mockito.mock(NoteViewModel::class.java)
    private val viewStateLiveData = MutableLiveData<NoteViewState>()

    private val testNote = Note("Id", "Title", "Text")

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
        doNothing().`when`(model).loadNote(Mockito.anyString())
        doNothing().`when`(model).save(Mockito.any())
        doNothing().`when`(model).delete(Mockito.any())

        Intent().apply {
            putExtra(NoteActivity::class.java.name + "extra.NOTE", testNote.id)
        }.let {
            activityTestRule.launchActivity(it)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun should_show_color_picker() {
//        onView(withId(R.id.pallete)).perform(click())
//        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

}