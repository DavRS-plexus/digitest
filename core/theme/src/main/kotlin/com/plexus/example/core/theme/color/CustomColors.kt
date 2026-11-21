package com.plexus.example.core.theme.color

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

open class CustomColors(
    val background: Color = Color.Unspecified,
    val topBarContainerColor: Color = Color.Unspecified,
    val topBarTitleContentColor: Color = Color.Unspecified,
    val topBarNavigationIconContentColor: Color = Color.Unspecified,
    val error: Color = Color.Unspecified,
)

internal object LightCustomColors : CustomColors(
    background = Palette.Light.md_theme_light_background,
    topBarContainerColor = Palette.Light.md_theme_light_primary,
    topBarTitleContentColor = Palette.Light.md_theme_light_icon,
    topBarNavigationIconContentColor = Palette.Light.md_theme_light_icon,
    error = Palette.Light.md_theme_light_error
)

internal object DarkCustomColors : CustomColors(
    background = Palette.Dark.md_theme_dark_background,
    topBarContainerColor = Palette.Dark.md_theme_dark_primary,
    topBarTitleContentColor = Palette.Dark.md_theme_dark_icon,
    topBarNavigationIconContentColor = Palette.Dark.md_theme_dark_icon,
    error = Palette.Dark.md_theme_dark_error
)
