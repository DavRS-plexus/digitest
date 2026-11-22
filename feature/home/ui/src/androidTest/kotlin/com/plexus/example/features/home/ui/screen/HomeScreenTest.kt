package com.plexus.example.features.home.ui.screen

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.plexus.example.core.resources.R
import com.plexus.example.core.theme.AppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun welcomeTextAndStartButtonAreDisplayed() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val welcomeText = context.getString(R.string.home_welcome)
        val startText = context.getString(R.string.home_start)

        composeRule.setContent {
            AppTheme(dynamicColor = false) {
                HomeScreen(onClickStart = {})
            }
        }

        composeRule.onNodeWithText(welcomeText).assertIsDisplayed()
        composeRule.onNode(
            hasText(startText) and SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button)
        ).assertIsDisplayed().assertHasClickAction()
    }
}
