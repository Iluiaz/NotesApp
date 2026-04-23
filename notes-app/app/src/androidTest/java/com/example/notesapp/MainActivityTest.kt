package com.example.notesapp

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setUp() {
        ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
    }

    @After
    fun tearDown() {
        ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
    }

    @Test
    fun createNote_displaysInList() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.addNoteFab)).perform(click())
        onView(withId(R.id.titleEditText)).perform(replaceText("UI note"), closeSoftKeyboard())
        onView(withId(R.id.contentEditText)).perform(replaceText("Created from espresso"), closeSoftKeyboard())
        onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click())

        onView(withText("UI note")).check(matches(isDisplayed()))
        onView(withText("Created from espresso")).check(matches(isDisplayed()))
    }

    @Test
    fun editNote_updatesDisplayedContent() {
        ActivityScenario.launch(MainActivity::class.java)

        createSeedNote()

        onView(withId(R.id.notesRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickChildViewWithId(R.id.editButton)
                )
            )
        onView(withId(R.id.titleEditText)).perform(replaceText("Edited note"), closeSoftKeyboard())
        onView(withId(R.id.contentEditText)).perform(replaceText("Edited body"), closeSoftKeyboard())
        onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click())

        onView(withText("Edited note")).check(matches(isDisplayed()))
        onView(withText("Edited body")).check(matches(isDisplayed()))
    }

    @Test
    fun deleteNote_removesDisplayedItem() {
        ActivityScenario.launch(MainActivity::class.java)

        createSeedNote()

        onView(withId(R.id.notesRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    clickChildViewWithId(R.id.deleteButton)
                )
            )
        onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click())

        onView(withText("Seed note")).check(doesNotExist())
        onView(withId(R.id.emptyStateTextView)).check(matches(isDisplayed()))
    }

    private fun createSeedNote() {
        onView(withId(R.id.addNoteFab)).perform(click())
        onView(withId(R.id.titleEditText)).perform(replaceText("Seed note"), closeSoftKeyboard())
        onView(withId(R.id.contentEditText)).perform(replaceText("Seed body"), closeSoftKeyboard())
        onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click())
    }
}
