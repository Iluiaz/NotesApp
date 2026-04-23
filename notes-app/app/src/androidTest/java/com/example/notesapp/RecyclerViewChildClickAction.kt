package com.example.notesapp

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers.any

fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = any(View::class.java)

        override fun getDescription(): String = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) {
            view.findViewById<View>(id).performClick()
            uiController.loopMainThreadUntilIdle()
        }
    }
}

