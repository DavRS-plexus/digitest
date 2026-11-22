package com.plexus.example.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.plexus.example.core.navigation.DigiviceRoot
import com.plexus.example.core.navigation.HomeGraph
import com.plexus.example.features.digivice.ui.navigation.digiviceNavGraph
import com.plexus.example.features.home.ui.navigation.homeNavGraph

@Composable
fun MainNavHost(innerPadding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeGraph,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {
        // NavGraphs of every feature
        homeNavGraph(
            navigateToDigivice = { navController.navigate(DigiviceRoot) },
        )
        digiviceNavGraph(navController = navController)
    }
}
