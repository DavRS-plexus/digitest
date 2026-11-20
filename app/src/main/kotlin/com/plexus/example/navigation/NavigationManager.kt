package com.plexus.example.navigation

import androidx.navigation.NavController
import com.plexus.example.core.navigation.Navigation

class NavigationManager : Navigation {
    override fun navigateTo(navController: NavController, route: Any) {
        navController.navigate(route)
    }

    override fun navigateUp(navController: NavController) {
        // TODO: navController.navigateUp()
    }
}