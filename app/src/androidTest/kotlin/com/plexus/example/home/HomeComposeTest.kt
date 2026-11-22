package com.plexus.example.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.plexus.example.core.resources.R
import com.plexus.example.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeComposeTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun startOpensDigivice() {
        val activity = composeRule.activity

        composeRule.onNodeWithText(activity.getString(R.string.home_welcome)).assertIsDisplayed()
        composeRule.onNodeWithText(activity.getString(R.string.home_start)).assertIsDisplayed().performClick()
        composeRule.onNodeWithContentDescription(activity.getString(R.string.digivice_search_button))
            .assertIsDisplayed()
    }
}
