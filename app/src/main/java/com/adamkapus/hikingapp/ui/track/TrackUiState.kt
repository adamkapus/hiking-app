package com.adamkapus.hikingapp.ui.track

sealed class TrackUiState {
    object Initial : TrackUiState()
    object ReadyToStart : TrackUiState()
    object TrackingInProgress : TrackUiState()
    object SavingRouteInProgress : TrackUiState()
    object SavingRouteSuccess : TrackUiState()
    object SavingRouteFailed : TrackUiState()
}