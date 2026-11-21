package com.plexus.example.features.digivice.ui.detail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.SubcomposeAsyncImage
import com.plexus.example.core.resources.R
import com.plexus.example.core.theme.AppTheme
import com.plexus.example.core.theme.color.LocalCustomColors
import com.plexus.example.core.theme.dimen.Size
import com.plexus.example.core.theme.dimen.Spacing
import com.plexus.example.core.theme.font.LocalTypography
import com.plexus.example.features.digivice.ui.detail.model.DigimonDetailUiModel
import com.plexus.example.features.digivice.ui.detail.state.DigimonDetailUiState

@Composable
internal fun DigiviceDetailScreen(uiState: DigimonDetailUiState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.background),
        contentAlignment = Alignment.Center,
    ) {
        when {
            uiState.isLoading -> StateLoading()
            uiState.isErrorVisible() -> StateFailure(uiState.errorMessage)
            uiState.detail != null -> StateSuccess(uiState.detail)
        }
    }
}

@Composable
private fun StateLoading() {
    CircularProgressIndicator(modifier = Modifier.size(Size.size_loading_big))
}

@Composable
private fun StateFailure(errorMessage: String?) {
    Text(
        text = stringResource(R.string.digimon_detail_error, errorMessage ?: ""),
        style = LocalTypography.bodyMedium,
        color = LocalCustomColors.current.error,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(Spacing.spacing_04),
    )
}

@Composable
private fun StateSuccess(detail: DigimonDetailUiModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.spacing_04),
        verticalArrangement = Arrangement.spacedBy(Spacing.spacing_04),
    ) {
        if (detail.existImage()) {
            SubcomposeAsyncImage(
                model = detail.imageUrl,
                contentDescription = detail.name,
                modifier = Modifier
                    .size(Size.size_image_poster)
                    .align(Alignment.CenterHorizontally),
                loading = { CircularProgressIndicator(modifier = Modifier.size(Size.size_loading_medium)) },
                error = { Text(stringResource(R.string.digivice_image_load_error)) },
            )
        }

        DetailField(stringResource(R.string.digimon_detail_name), detail.name)
        DetailField(stringResource(R.string.digimon_detail_level), detail.level)
        DetailField(stringResource(R.string.digimon_detail_type), detail.types)
        DetailField(stringResource(R.string.digimon_detail_attribute), detail.attributes)
        DetailField(stringResource(R.string.digimon_detail_description), detail.description)
    }
}

@Composable
private fun DetailField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = LocalTypography.titleLarge)
        if (value.isBlank()) {
            Text(stringResource(R.string.digimon_detail_unavailable))
        } else {
            Text(text = value, style = LocalTypography.bodyMedium)
        }
    }
}

@Composable
private fun DetailField(label: String, values: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = LocalTypography.titleLarge)
        if (values.isEmpty()) {
            Text(stringResource(R.string.digimon_detail_unavailable))
        } else {
            values.forEach {
                Text(text = it, style = LocalTypography.bodyMedium)
            }
        }
    }
}

// region Previews

@Preview(name = "Detail", showBackground = true)
@Preview(name = "Detail - font 130%", showBackground = true, fontScale = 1.3f)
@Preview(name = "Detail - font 200%", showBackground = true, fontScale = 2f)
@Composable
private fun DigiviceDetailPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceDetailScreen(
            DigimonDetailUiState(
                isLoading = false,
                detail = DigimonDetailUiModel(
                    name = "Agumon",
                    imageUrl = null,
                    level = "Child",
                    types = listOf("Reptile"),
                    attributes = listOf("Vaccine"),
                    description = "A small dinosaur-like Digimon.",
                ),
            )
        )
    }
}

@Preview(name = "Loading", showBackground = true)
@Preview(name = "Loading - font 130%", showBackground = true, fontScale = 1.3f)
@Preview(name = "Loading - font 200%", showBackground = true, fontScale = 2f)
@Composable
private fun DigiviceDetailLoadingPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceDetailScreen(DigimonDetailUiState())
    }
}

@Preview(name = "Blank detail fields", showBackground = true)
@Preview(name = "Blank detail fields - font 130%", showBackground = true, fontScale = 1.3f)
@Preview(name = "Blank detail fields - font 200%", showBackground = true, fontScale = 2f)
@Composable
private fun DigiviceDetailBlankFieldsPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceDetailScreen(
            DigimonDetailUiState(
                isLoading = false,
                detail = DigimonDetailUiModel(
                    name = "",
                    imageUrl = null,
                    level = "",
                    types = emptyList(),
                    attributes = emptyList(),
                    description = "",
                ),
            )
        )
    }
}

@Preview(name = "Error", showBackground = true)
@Preview(name = "Error - font 130%", showBackground = true, fontScale = 1.3f)
@Preview(name = "Error - font 200%", showBackground = true, fontScale = 2f)
@Composable
private fun DigiviceDetailErrorPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceDetailScreen(DigimonDetailUiState(isLoading = false, errorMessage = "HTTP 404"))
    }
}

// endregion
