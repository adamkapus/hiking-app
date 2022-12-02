package com.adamkapus.hikingapp.ui.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamkapus.hikingapp.domain.interactor.gpx.GpxInteractor
import com.adamkapus.hikingapp.domain.interactor.location.LocationInteractor
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResult
import com.adamkapus.hikingapp.domain.model.map.Route
import com.adamkapus.hikingapp.ui.map.MapUiState.Initial
import com.adamkapus.hikingapp.ui.map.MapUiState.RouteLoaded
import com.adamkapus.hikingapp.utils.FlowerRarity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {
    private val userPosition = MutableStateFlow<Location?>(null)
    private val flowerLocationState = MutableStateFlow<FlowerLocationState>(
        FlowerLocationState(
            emptySet()
        )
    )
    private val route = MutableStateFlow<Route?>(null)


    val uiState = combine(
        userPosition,
        flowerLocationState,
        route
    ) { userPosition, flowerLocationState, route ->
        if (route != null) {
            RouteLoaded(
                flowerLocationState = flowerLocationState,
                route = route
            )
        } else {
            Initial(
                flowerLocationState = flowerLocationState,
                userPosition = userPosition
            )
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), initialValue = MapUiState.Initial(
            flowerLocationState = FlowerLocationState(emptySet()),
            null
        )
    )

    private val _locationPermissionsDeniedEvent = MutableStateFlow(false)
    val locationPermissionsDeniedEvent = _locationPermissionsDeniedEvent.asStateFlow()

    //fun on

    fun loadUserPosition() = viewModelScope.launch {
        Log.d("PLS", "loaduserpositions")
        val inter = LocationInteractor()
        val resp = inter.getLastLocation()
        Log.d("PLS", "resp" + resp.toString())
        when (resp) {
            is InteractorResult -> {
                if (resp.result != null) {
                    userPosition.update { resp.result }
                }
            }
            is InteractorError -> {}
        }
    }

    fun onFlowerLocationStateChanged(visibleCategories: Set<FlowerRarity>) {
        flowerLocationState.update { FlowerLocationState(visibleCategories) }
    }

    fun loadGpxFile(inputStream: InputStream) = viewModelScope.launch {
        val inter = GpxInteractor()
        val resp = inter.parseFile(inputStream)
        route.update { Route(null, null, resp) }
    }

    //ToDo
    fun onLocationPermissionsDenied() {

    }

    //ToDo
    fun handledLocationPermissionsDenied() {

    }

}