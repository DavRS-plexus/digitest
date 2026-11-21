package com.plexus.example.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.plexus.example.core.navigation.DigiviceGraph
import com.plexus.example.features.digivice.ui.navigation.digiviceNavGraph

@Composable
fun MainNavHost(
    innerPadding: PaddingValues,
    navController: NavHostController,
    navigationManager: NavigationManager
) {
    NavHost(
        navController = navController,
        startDestination = DigiviceGraph,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        // Each feature registers its own graph
        // :app ONLY calls these functions - doesn't know about internal screens
        digiviceNavGraph(navController = navController, navigationManager = navigationManager)
    }
}