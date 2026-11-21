package com.plexus.example.features.digivice.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.plexus.example.features.digivice.ui.search.screen.DigiviceSearchScreen
import com.plexus.example.features.digivice.ui.search.state.DigiviceSearchUiEvent
import com.plexus.example.features.digivice.ui.search.viewmodel.DigiviceSearchViewModel
import com.plexus.example.features.digivice.ui.search.viewmodel.DigiviceSearchViewModel.UserAction.OnClickItem
import com.plexus.example.features.digivice.ui.search.viewmodel.DigiviceSearchViewModel.UserAction.OnClickSearch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun DigiviceListEntryPoint(
    navigateToDetail: (digimonId: String) -> Unit
) {
    val searchViewModel: DigiviceSearchViewModel = koinViewModel()
    val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()

    HandleUiEvents(
        viewModel = searchViewModel,
        navigateToDetail = navigateToDetail,
    )

    DigiviceSearchScreen(
        uiState = uiState,
        onClickSearch = { name ->
            searchViewModel.onAction(OnClickSearch(name))
        },
        onClickItem = { id ->
            searchViewModel.onAction(OnClickItem(id))
        },
    )
}

@Composable
private fun HandleUiEvents(
    viewModel: DigiviceSearchViewModel,
    navigateToDetail: (digimonId: String) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is DigiviceSearchUiEvent.NavigateToDetail -> navigateToDetail(event.digimonId)
                }
            }
        }
    }
}
