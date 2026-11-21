package com.plexus.example.features.digivice.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.plexus.example.core.navigation.DigiviceGraph
import com.plexus.example.core.navigation.DigiviceRoot
import com.plexus.example.core.navigation.Navigation
import com.plexus.example.features.digivice.ui.search.DigiviceListEntryPoint
import com.plexus.example.features.digivice.ui.detail.DigiviceDetailEntryPoint
import com.plexus.example.features.digivice.ui.navigation.DigiviceInternalRoute.*

fun NavGraphBuilder.digiviceNavGraph(
    navController: NavHostController,
    navigationManager: Navigation
) {
    navigation<DigiviceGraph>(
        startDestination = DigiviceRoot,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        // Search
        composable<DigiviceRoot> {
            DigiviceListEntryPoint(
                navigateToDetail = { digimonId ->
                    navController.navigate(DigiviceDetailRoute(digimonId))
                }
            )
        }

        // Detail
        composable<DigiviceDetailRoute> {
            DigiviceDetailEntryPoint()
        }
    }
}
