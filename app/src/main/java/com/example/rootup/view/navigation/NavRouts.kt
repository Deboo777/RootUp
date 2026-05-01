package com.example.rootup.view.navigation

import kotlinx.serialization.Serializable


@Serializable
data class Detait(val plantId: Int)

@Serializable
data object AddPlantRoute
@Serializable
data object Home

@Serializable
data object Office

@Serializable
data object Login

@Serializable
data object Registration

@Serializable
data object Details

@Serializable
data class PlantStats(val plantId: Int)
