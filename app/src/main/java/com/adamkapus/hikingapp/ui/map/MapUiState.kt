package com.adamkapus.hikingapp.ui.map

import android.location.Location
import com.adamkapus.hikingapp.domain.model.map.Route
import com.adamkapus.hikingapp.ui.map.model.FlowerOnMap
import com.adamkapus.hikingapp.utils.FlowerRarity

sealed class MapUiState(
    flowerVisibilityState: FlowerVisibiltyState,
    flowers: List<FlowerOnMap>
) {
    data class Initial(
        val flowerVisibilityState: FlowerVisibiltyState,
        val flowers: List<FlowerOnMap>,
        val userPosition: Location?
    ) :
        MapUiState(flowerVisibilityState = flowerVisibilityState, flowers = flowers)

    data class RouteLoaded(
        val flowerVisibilityState: FlowerVisibiltyState,
        val flowers: List<FlowerOnMap>,
        val route: Route
    ) :
        MapUiState(flowerVisibilityState = flowerVisibilityState, flowers = flowers)
}


data class FlowerVisibiltyState(val visibleCategories: Set<FlowerRarity>)
