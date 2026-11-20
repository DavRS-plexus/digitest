package com.plexus.example.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.plexus.example.navigation.MainNavHost
import com.plexus.example.navigation.NavigationManager

@Composable
fun MainApp(
    navigationManager: NavigationManager
) {
    val navController = rememberNavController()

    // Get current back stack entry
//    val backStackEntry by navController.currentBackStackEntryAsState()
//    // Get the name of the current screen
//    val currentScreen = CupcakeScreen.valueOf(
//        backStackEntry?.destination?.route ?: CupcakeScreen.Start.name
//    )

    Scaffold(
        topBar = {
//            CupcakeAppBar(
//                currentScreen = currentScreen,
//                canNavigateBack = navController.previousBackStackEntry != null,
//                navigateUp = { navController.navigateUp() }
//            )
        }
    ) { innerPadding ->
        MainNavHost(
            innerPadding = innerPadding,
            navController = navController,
            navigationManager = navigationManager
        )
    }
}