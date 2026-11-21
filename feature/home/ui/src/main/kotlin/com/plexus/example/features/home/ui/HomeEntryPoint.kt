package com.plexus.example.features.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.plexus.example.features.home.ui.screen.HomeScreen
import com.plexus.example.features.home.ui.state.HomeUiEvent
import com.plexus.example.features.home.ui.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun HomeEntryPoint(navigateToDigivice: () -> Unit) {
    val viewModel: HomeViewModel = koinViewModel()

    HandleUiEvents(viewModel = viewModel, navigateToDigivice = navigateToDigivice)

    HomeScreen(onClickStart = { viewModel.onAction(HomeViewModel.UserAction.OnClickStart) })
}

@Composable
private fun HandleUiEvents(viewModel: HomeViewModel, navigateToDigivice: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    HomeUiEvent.NavigateToDigivice -> navigateToDigivice()
                }
            }
        }
    }
}
