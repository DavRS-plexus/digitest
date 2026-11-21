package com.plexus.example.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.plexus.example.core.theme.AppTheme
import com.plexus.example.navigation.NavigationManager

class MainActivity : ComponentActivity() {

    // TODO: Inject:
    private val navigationManager = NavigationManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainApp(navigationManager = navigationManager)
            }
        }
    }
}