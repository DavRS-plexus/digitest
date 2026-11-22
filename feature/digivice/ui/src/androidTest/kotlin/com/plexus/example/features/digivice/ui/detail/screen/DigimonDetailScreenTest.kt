package com.plexus.example.features.digivice.ui.detail.screen

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.plexus.example.core.resources.R
import com.plexus.example.core.theme.AppTheme
import com.plexus.example.features.digivice.ui.detail.model.DigimonDetailUiModel
import com.plexus.example.features.digivice.ui.detail.state.DigimonDetailUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DigimonDetailScreenTest {
    @get:Rule
    val composeRule = createComposeRule()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun loadingDisplaysProgressIndicator() {
        showScreen(DigimonDetailUiState(isLoading = true))

        composeRule.onNode(SemanticsMatcher.keyIsDefined(SemanticsProperties.ProgressBarRangeInfo))
            .assertIsDisplayed()
    }

    @Test
    fun errorDisplaysErrorMessage() {
        showScreen(DigimonDetailUiState(isLoading = false, errorMessage = "Detail failed"))

        composeRule.onNodeWithText(context.getString(R.string.digimon_detail_error, "Detail failed"))
            .assertIsDisplayed()
    }

    @Test
    fun detailDisplaysAllFields() {
        val detail = DigimonDetailUiModel(
            name = "Agumon",
            imageUrl = null,
            level = "Child",
            types = listOf("Reptile"),
            attributes = listOf("Vaccine"),
            description = "A small dinosaur-like Digimon.",
        )
        showScreen(DigimonDetailUiState(isLoading = false, detail = detail))

        listOf(
            R.string.digimon_detail_name,
            R.string.digimon_detail_level,
            R.string.digimon_detail_type,
            R.string.digimon_detail_attribute,
            R.string.digimon_detail_description,
        ).forEach {
            composeRule.onNodeWithText(context.getString(it)).performScrollTo().assertIsDisplayed()
        }
        listOf(detail.name, detail.level, "Reptile", "Vaccine", detail.description).forEach {
            composeRule.onNodeWithText(it).performScrollTo().assertIsDisplayed()
        }
    }

    @Test
    fun missingFieldsDisplayUnavailableMessage() {
        val detail = DigimonDetailUiModel("Agumon", null, "", emptyList(), emptyList(), "")
        showScreen(DigimonDetailUiState(isLoading = false, detail = detail))

        composeRule.onAllNodesWithText(context.getString(R.string.digimon_detail_unavailable))
            .assertCountEquals(4)
    }

    private fun showScreen(state: DigimonDetailUiState) {
        composeRule.setContent {
            AppTheme(dynamicColor = false) {
                DigiviceDetailScreen(state)
            }
        }
    }
}
