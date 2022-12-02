package com.adamkapus.hikingapp.ui.map

import android.location.Location
import com.adamkapus.hikingapp.domain.model.map.Route
import com.adamkapus.hikingapp.utils.FlowerRarity
import com.google.android.gms.maps.model.LatLng

sealed class MapUiState(flowerLocationState: FlowerLocationState) {
    data class Initial(val flowerLocationState: FlowerLocationState, val userPosition: Location?) :
        MapUiState(flowerLocationState = flowerLocationState)

    data class RouteLoaded(val flowerLocationState: FlowerLocationState, val route: Route) :
        MapUiState(flowerLocationState = flowerLocationState)
}


data class FlowerLocationState(val visibleCategories: Set<FlowerRarity>)
