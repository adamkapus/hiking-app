package com.adamkapus.hikingapp.ui.track

import android.util.Log
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

    /*
    private val _route = MutableLiveData<List<LatLng>>()
    val route: LiveData<List<LatLng>> = _route

    fun addRoute(newRoute: List<Location>) = viewModelScope.launch {
        val route: MutableList<LatLng> = mutableListOf()
        for (location in newRoute) {
            route.add(LatLng(location.latitude, location.longitude))
        }
        _route.postValue(route)
    }

    fun clearRoute() = viewModelScope.launch {
        _route.postValue(mutableListOf())
    }*/

    private val _uiState = MutableStateFlow<TrackUiState>(Initial)
    val uiState: StateFlow<TrackUiState> = _uiState.asStateFlow()


    //ToDo onpermission denied event

    fun initUiState() = viewModelScope.launch {
        val resp = trackingInteractor.isTrackingInProgress()
        when (resp) {
            is InteractorError -> {
                _uiState.update { ReadyToStart }
            }
            is InteractorResult -> {
                if (resp.result) {
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
        Log.d("PLS", "track start")
        trackingInteractor.startTracking()
        _uiState.update { TrackingInProgress }
    }

    fun cancelTracking() = viewModelScope.launch {
        Log.d("PLS", "track cancel")
        trackingInteractor.stopTracking()
        _uiState.update { ReadyToStart }
    }

    fun saveRoute() = viewModelScope.launch {
        _uiState.update { SavingRouteInProgress }
        Log.d("PLS", "track save")
        val resp = trackingInteractor.saveRoute()
        when (resp) {
            is InteractorError -> {
                _uiState.update { SavingRouteFailed }
            }
            is InteractorResult -> {
                _uiState.update { SavingRouteSuccess }
            }
        }
        //ToDo remove
        _uiState.update { ReadyToStart }
    }

    fun onPermissionDenied() = viewModelScope.launch {

    }

    fun handledTrackingFailed() = viewModelScope.launch {

    }

    fun handledTrackingSuccess() = viewModelScope.launch {

    }

}