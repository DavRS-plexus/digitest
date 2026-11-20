package com.plexus.example.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.plexus.example.core.navigation.HomeGraph

@Composable
fun MainNavHost(
    innerPadding: PaddingValues,
    navController: NavHostController,
    navigationManager: NavigationManager
) {
    NavHost(
        navController = navController,
        startDestination = HomeGraph,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(innerPadding)
    ) {
        // Each feature registers its own graph
        // :app ONLY calls these functions - doesn't know about internal screens
//        homeNavGraph(navController = navController)
//        infoNavGraph(navController = navController)
//        settingsNavGraph(navController = navController, navigationManager = navigationManager)
    }
}