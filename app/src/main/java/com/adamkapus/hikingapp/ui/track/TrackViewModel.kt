package com.adamkapus.hikingapp.ui.track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamkapus.hikingapp.domain.interactor.tracking.TrackingInteractor
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResult
import com.adamkapus.hikingapp.ui.track.TrackUiState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    private val trackingInteractor: TrackingInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrackUiState>(Initial)
    val uiState: StateFlow<TrackUiState> = _uiState.asStateFlow()

    fun initUiState() = viewModelScope.launch {
        when (val response = trackingInteractor.isTrackingInProgress()) {
            is InteractorError -> {
                _uiState.update { ReadyToStart }
            }
            is InteractorResult -> {
                if (response.result) {
                    _uiState.update {
                        TrackingInProgress
                    }
                } else {
                    _uiState.update {
                        ReadyToStart
                    }
                }
            }
        }
    }

    fun trackingStarted() = viewModelScope.launch {
        trackingInteractor.startTracking()
        _uiState.update { TrackingInProgress }
    }

    fun cancelTracking() = viewModelScope.launch {
        trackingInteractor.cancelTracking()
        _uiState.update { ReadyToStart }
    }

    fun saveRoute(name : String) = viewModelScope.launch {
        _uiState.update { SavingRouteInProgress }
        when (trackingInteractor.saveRoute(name)) {
            is InteractorError -> {
                _uiState.update { SavingRouteFailed }
            }
            is InteractorResult -> {
                _uiState.update { SavingRouteSuccess }
            }
        }
    }


    fun handledTrackingFailed() = viewModelScope.launch {
        _uiState.update { ReadyToStart }
    }

    fun handledTrackingSuccess() = viewModelScope.launch {
        _uiState.update { ReadyToStart }
    }

}