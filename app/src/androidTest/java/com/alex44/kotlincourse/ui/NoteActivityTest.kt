package com.alex44.kotlincourse.ui

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import com.alex44.kotlincourse.R
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.view.ui.activities.NoteActivity
import com.alex44.kotlincourse.viewmodel.NoteViewModel
import com.alex44.kotlincourse.viewmodel.states.NoteViewState
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.mockito.AdditionalMatchers.not

class NoteActivityTest {
    @get:Rule
    val activityTestRule = IntentsTestRule(NoteActivity::class.java, true, false)

    private val model: NoteViewModel = mockk(relaxed = true)
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

        every { model.getViewState() } returns viewStateLiveData
        every { model.loadNote(testNote.id) } just runs
        every { model.save(testNote) } just runs
        every { model.delete(testNote) } just runs

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
        onView(withId(R.id.pallete)).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

}