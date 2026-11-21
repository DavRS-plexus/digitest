package com.plexus.example.features.home.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.plexus.example.core.navigation.DigiviceRoot
import com.plexus.example.core.navigation.HomeGraph
import com.plexus.example.core.navigation.HomeRoot
import com.plexus.example.features.home.ui.HomeEntryPoint

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController,
    navigateToDigivice: () -> Unit,
) {
    navigation<HomeGraph>(
        startDestination = HomeRoot,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        composable<HomeRoot> {
            HomeEntryPoint(
                navigateToDigivice = navigateToDigivice
            )
        }
    }
}
