package com.plexus.example.features.digivice.ui.search.screen

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.plexus.example.core.resources.R
import com.plexus.example.core.theme.AppTheme
import com.plexus.example.features.digivice.ui.search.model.DigimonItemUiModel
import com.plexus.example.features.digivice.ui.search.state.DigiviceSearchUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DigiviceSearchScreenTest {
    @get:Rule
    val composeRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun searchButtonAndImeSubmitTheSameText() {
        val queries = mutableListOf<String>()
        showScreen(DigiviceSearchUiState(isLoading = false), onSearch = { queries.add(it) })

        composeRule.onNode(hasSetTextAction()).performTextInput("Agumon")
        composeRule.onNodeWithContentDescription(context.getString(R.string.digivice_search_button)).performClick()
        composeRule.onNode(hasSetTextAction()).performImeAction()

        composeRule.runOnIdle { assertEquals(listOf("Agumon", "Agumon"), queries) }
    }

    @Test
    fun emptyResultsDisplayEmptyMessage() {
        showScreen(DigiviceSearchUiState(isLoading = false))

        composeRule.onNodeWithText(context.getString(R.string.digivice_search_empty)).assertIsDisplayed()
    }

    @Test
    fun errorDisplaysErrorMessage() {
        showScreen(DigiviceSearchUiState(isLoading = false, errorMessage = "Search failed"))

        composeRule.onNodeWithText(context.getString(R.string.digivice_search_error, "Search failed"))
            .assertIsDisplayed()
    }

    @Test
    fun loadingDisplaysProgressIndicator() {
        showScreen(DigiviceSearchUiState(isLoading = true))

        composeRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo))
            .assertIsDisplayed()
    }

    @Test
    fun selectingResultPassesItsId() {
        var selectedId: String? = null
        showScreen(
            DigiviceSearchUiState(isLoading = false, items = listOf(DigimonItemUiModel("1", "Agumon", ""))),
            onItem = { selectedId = it },
        )

        composeRule.onNodeWithText("Agumon").assertIsDisplayed().performClick()

        composeRule.runOnIdle { assertEquals("1", selectedId) }
    }

    @Test
    fun submittingSearchResetsScrollToTop() {
        val items = (0 until 50).map { DigimonItemUiModel("$it", "Digimon $it", "") }
        showScreen(DigiviceSearchUiState(isLoading = false, items = items))
        composeRule.onNode(hasScrollToIndexAction()).performScrollToIndex(40)
        composeRule.onNodeWithText("Digimon 40").assertIsDisplayed()

        composeRule.onNodeWithContentDescription(context.getString(R.string.digivice_search_button)).performClick()

        composeRule.onNodeWithText("Digimon 0").assertIsDisplayed()
    }

    private fun showScreen(
        state: DigiviceSearchUiState,
        onSearch: (String) -> Unit = {},
        onItem: (String) -> Unit = {},
    ) {
        composeRule.setContent {
            AppTheme(dynamicColor = false) {
                DigiviceSearchScreen(state, onClickItem = onItem, onClickSearch = onSearch)
            }
        }
    }
}
