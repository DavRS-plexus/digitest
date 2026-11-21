package com.plexus.example.features.digivice.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plexus.example.features.digivice.ui.detail.screen.DigiviceDetailScreen
import com.plexus.example.features.digivice.ui.detail.viewmodel.DigimonDetailViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun DigiviceDetailEntryPoint() {
    val viewModel: DigimonDetailViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DigiviceDetailScreen(uiState = uiState)
}
