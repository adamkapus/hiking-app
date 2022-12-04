package com.adamkapus.hikingapp.ui.list

import com.adamkapus.hikingapp.domain.model.map.Route

sealed class HomeUiState {
    object Initial : HomeUiState()
    data class RoutesLoaded(val routes: List<Route>) : HomeUiState()
}