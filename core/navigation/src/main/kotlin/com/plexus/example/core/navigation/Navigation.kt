package com.plexus.example.core.navigation

import androidx.navigation.NavController


interface Navigation {
    fun navigateTo(navController: NavController, route: Any)

    fun navigateUp(navController: NavController)

    // ... other navigation methods can be added here as needed
}
