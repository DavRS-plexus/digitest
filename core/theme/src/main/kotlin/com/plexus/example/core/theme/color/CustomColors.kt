package com.plexus.example.core.theme.color

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

open class CustomColors(
    val background: Color = Color.Unspecified,
    // ... other color properties
)

internal object LightCustomColors : CustomColors(
    background = Palette.Light.md_theme_light_background
)

internal object DarkCustomColors : CustomColors(
    background = Palette.Dark.md_theme_dark_background
)
