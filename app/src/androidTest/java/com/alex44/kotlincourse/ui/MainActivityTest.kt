package com.alex44.kotlincourse.ui

import androidx.test.espresso.intent.rule.IntentsTestRule
import com.alex44.kotlincourse.model.NoteResult
import com.alex44.kotlincourse.model.dtos.Note
import com.alex44.kotlincourse.view.ui.activities.MainActivity
import com.alex44.kotlincourse.viewmodel.MainViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin

class MainActivityTest {
    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val model: MainViewModel = mockk(relaxed = true)
    private val testNotes = listOf(
            Note("Id1", "Title1", "Text1"),
            Note("Id2", "Title2", "Text2"),
            Note("Id3", "Title3", "Text3")
    )
    private val channel : ReceiveChannel<List<Note>> = Channel<List<Note>>(Channel.CONFLATED).apply{
        offer(testNotes)
    }
    private val channel1 = Channel<NoteResult>(Channel.CONFLATED)

    @Before
    fun setup() {
        loadKoinModules(
                listOf(
                        module {
                            viewModel(override = true) { model }
                        }
                )
        )

        every { model.getViewState() } returns channel
        activityTestRule.launchActivity(null)
        every { model.notesChannel } returns channel1
        //viewStateLiveData.postValue(MainViewState(notes = testNotes))
    }

    @After
    fun tearDown(){
        stopKoin()
    }

    @Test
    fun check_data_is_displayed(){
//        onView(withId(R.id.rv_notes)).perform(scrollToPosition<NotesRvAdapter.ViewHolder>(1))
//        onView(withText(testNotes[1].text)).check(matches(isDisplayed()))
    }

}