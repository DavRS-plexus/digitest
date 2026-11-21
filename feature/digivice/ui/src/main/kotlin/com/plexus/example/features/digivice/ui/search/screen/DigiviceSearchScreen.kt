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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.plexus.example.core.resources.R
import com.plexus.example.core.theme.AppTheme
import com.plexus.example.core.theme.color.LocalCustomColors
import com.plexus.example.features.digivice.ui.search.model.DigimonItemUiModel
import com.plexus.example.features.digivice.ui.search.state.DigiviceSearchUiState

@Composable
internal fun DigiviceSearchScreen(
    uiState: DigiviceSearchUiState,
    onBack: () -> Unit,
    onClickItem: (digimonId: String) -> Unit,
    onClickSearch: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = LocalCustomColors.current.background)
    ) {
        TopSearch(onClickSearch = onClickSearch)
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
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

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        label = { Text(stringResource(R.string.digivice_search_name)) },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { onClickSearch(searchText) }) {
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
        Text(text = stringResource(R.string.digivice_search_error) + ": $message")
    }
}

@Composable
private fun StateEmpty() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(R.string.digivice_search_empty))
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
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClickItem(item.id) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SubcomposeAsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    loading = { CircularProgressIndicator(modifier = Modifier.size(24.dp)) },
                    error = {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.digivice_image_load_error),
                        )
                    },
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "${item.id} - ${item.name}")
            }
        }
    }
}

// region Previews

private val previewItems = listOf(
    DigimonItemUiModel(id = "1", name = "Agumon", imageUrl = ""),
    DigimonItemUiModel(id = "2", name = "Airdramon", imageUrl = ""),
    DigimonItemUiModel(id = "3", name = "Angemon", imageUrl = ""),
)

@Preview(name = "Empty", showBackground = true)
@Composable
private fun DigiviceSearchEmptyPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceSearchScreen(
            uiState = DigiviceSearchUiState(isLoading = false),
            onBack = {},
            onClickItem = {},
            onClickSearch = {},
        )
    }
}

@Preview(name = "Error", showBackground = true)
@Composable
private fun DigiviceSearchErrorPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceSearchScreen(
            uiState = DigiviceSearchUiState(
                isLoading = false,
                items = previewItems,
                errorMessage = "Unable to load Digimon",
            ),
            onBack = {},
            onClickItem = {},
            onClickSearch = {},
        )
    }
}

@Preview(name = "Success", showBackground = true)
@Composable
private fun DigiviceSearchSuccessPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceSearchScreen(
            uiState = DigiviceSearchUiState(isLoading = false, items = previewItems),
            onBack = {},
            onClickItem = {},
            onClickSearch = {},
        )
    }
}

@Preview(name = "Loading", showBackground = true)
@Composable
private fun DigiviceSearchLoadingPreview() {
    AppTheme(dynamicColor = false) {
        DigiviceSearchScreen(
            uiState = DigiviceSearchUiState(isLoading = true),
            onBack = {},
            onClickItem = {},
            onClickSearch = {},
        )
    }
}

// endregion
