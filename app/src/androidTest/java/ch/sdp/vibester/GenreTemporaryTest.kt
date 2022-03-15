package ch.sdp.vibester

import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.arrayWithSize
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


//        btnRock.setOnClickListener {
//            val songs_query1 = SongsList(LastfmApi.querySongsByTag(OkHttpClient(),"rock").get())
//            val arr = ArrayAdapter(this, android.R.layout.simple_list_item_1 , songs_query1.getSongs())
//            listSongs.adapter = arr
//        }
class GenreTemporaryTest {

    @get:Rule
    val testRule = ActivityScenarioRule(
        GenreTemporary::class.java
    )

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun clean() {
        Intents.release()
    }

    @Test
    fun buttonLayoutIsDisplayed() {
        onView(withId(R.id.rock)).check(matches(isDisplayed()))
        onView(withId(R.id.kpop)).check(matches(isDisplayed()))
        onView(withId(R.id.top)).check(matches(isDisplayed()))

    }

    @Test
    fun songsListLayoutIsDisplayed() {
        onView(withId(R.id.listSongs)).check(matches(isDisplayed()))
    }

    @Test
    fun checkListLayoutRock() {
        onView(withId(R.id.rock)).perform(ViewActions.click())
        //something should happen here
//        Thread.sleep(1000)
//        onView(withId(R.id.listSongs)).check(matches(Matchers.notNullValue()));

    }
}