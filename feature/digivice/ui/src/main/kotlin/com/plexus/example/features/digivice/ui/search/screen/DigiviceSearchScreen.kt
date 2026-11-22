package com.plexus.example.features.digivice.ui.search.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.SubcomposeAsyncImage
import com.plexus.example.core.resources.R
import com.plexus.example.core.theme.AppTheme
import com.plexus.example.core.theme.color.LocalCustomColors
import com.plexus.example.core.theme.dimen.Size
import com.plexus.example.core.theme.dimen.Spacing
import com.plexus.example.core.theme.font.LocalTypography
import com.plexus.example.features.digivice.ui.search.model.DigimonItemUiModel
import com.plexus.example.features.digivice.ui.search.state.DigiviceSearchUiState

@Composable
internal fun DigiviceSearchScreen(
    uiState: DigiviceSearchUiState,
    onClickItem: (digimonId: String) -> Unit,
    onClickSearch: (name: String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LocalCustomColors.current.background)
    ) {
        TopSearch(onClickSearch = onClickSearch)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                uiState.isEmptyList() -> StateEmpty()
                uiState.isErrorVisible() -> StateError(message = uiState.errorMessage)
                else -> StateSuccess(
                    items = uiState.items,
                    isLoading = uiState.isLoading,
                    onClickItem = onClickItem,
                )
            }
        }
    }
}

@Composable
private fun TopSearch(onClickSearch: (String) -> Unit) {
    var searchText by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val performSearch: () -> Unit = {
        keyboardController?.hide()
        onClickSearch(searchText)
    }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.spacing_04),
        label = { Text(stringResource(R.string.digivice_search_name)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { performSearch() }),
        trailingIcon = {
            IconButton(onClick = performSearch) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.digivice_search_button),
                )
            }
        },
    )
}

@Composable
private fun StateError(message: String?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.digivice_search_error, message ?: ""),
            style = LocalTypography.bodyMedium,
            color = LocalCustomColors.current.error,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StateEmpty() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.digivice_search_empty),
            style = LocalTypography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun StateSuccess(
    items: List<DigimonItemUiModel>,
    isLoading: Boolean,
    onClickItem: (digimonId: String) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.spacing_04),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(Size.size_loading_big))
                }
            }
        }
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickItem(item.id) }
                    .padding(Spacing.spacing_04),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SubcomposeAsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(Size.size_image_thumbnail),
                    loading = { CircularProgressIndicator(modifier = Modifier.size(Size.size_loading_small)) },
                    error = {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.digivice_image_load_error),
                        )
                    },
                )
                Spacer(modifier = Modifier.width(Spacing.spacing_04))
                Text(text = item.name, style = LocalTypography.titleLarge)
            }
        }
    }
}

// region Previews

@Preview(name = "Empty", showBackground = true)
@Preview(name = "Empty - font 130%", showBackground = true, fontScale = 1.3f)
@Preview(name = "Empty - font 200%", showBackground = true, fontScale = 2f)
@Composable
private fun DigiviceSearchEmptyPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceSearchScreen(
            uiState = DigiviceSearchUiState(isLoading = false),
            onClickItem = {},
            onClickSearch = {},
        )
    }
}

@Preview(name = "Error", showBackground = true)
@Preview(name = "Error - font 130%", showBackground = true, fontScale = 1.3f)
@Preview(name = "Error - font 200%", showBackground = true, fontScale = 2f)
@Composable
private fun DigiviceSearchErrorPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceSearchScreen(
            uiState = DigiviceSearchUiState(
                isLoading = false,
                items = previewItems,
                errorMessage = "Unable to load Digimon",
            ),
            onClickItem = {},
            onClickSearch = {},
        )
    }
}

private val previewItems = listOf(
    DigimonItemUiModel(id = "1", name = "Agumon", imageUrl = ""),
    DigimonItemUiModel(id = "2", name = "Airdramon", imageUrl = ""),
    DigimonItemUiModel(id = "3", name = "Angemon", imageUrl = ""),
)

@Preview(name = "Success", showBackground = true)
@Preview(name = "Success - font 130%", showBackground = true, fontScale = 1.3f)
@Preview(name = "Success - font 200%", showBackground = true, fontScale = 2f)
@Composable
private fun DigiviceSearchSuccessPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceSearchScreen(
            uiState = DigiviceSearchUiState(isLoading = false, items = previewItems),
            onClickItem = {},
            onClickSearch = {},
        )
    }
}

@Preview(name = "Loading", showBackground = true)
@Preview(name = "Loading - font 130%", showBackground = true, fontScale = 1.3f)
@Preview(name = "Loading - font 200%", showBackground = true, fontScale = 2f)
@Composable
private fun DigiviceSearchLoadingPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceSearchScreen(
            uiState = DigiviceSearchUiState(isLoading = true),
            onClickItem = {},
            onClickSearch = {},
        )
    }
}

// endregion
