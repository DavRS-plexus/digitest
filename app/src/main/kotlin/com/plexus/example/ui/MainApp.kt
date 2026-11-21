package com.plexus.example.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.plexus.example.core.resources.R
import com.plexus.example.core.theme.color.LocalCustomColors
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
            TopBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        MainNavHost(
            innerPadding = innerPadding,
            navController = navController,
            navigationManager = navigationManager
        )
    }
}

@Composable
private fun TopBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit
) {
    TopAppBar(
        title = { Text("HOLA") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = LocalCustomColors.current.topBarContainerColor,
            titleContentColor = LocalCustomColors.current.topBarTitleContentColor,
            navigationIconContentColor = LocalCustomColors.current.topBarNavigationIconContentColor
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}