package com.plexus.example.features.digivice.ui.navigation

import kotlinx.serialization.Serializable

internal sealed interface DigiviceInternalRoute {

    @Serializable
    data class DigiviceDetailRoute(
        val digimonId: String,
    ) : DigiviceInternalRoute
}
