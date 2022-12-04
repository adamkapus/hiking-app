package com.adamkapus.hikingapp.ui.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adamkapus.hikingapp.domain.interactor.gpx.GpxInteractor
import com.adamkapus.hikingapp.domain.interactor.location.LocationInteractor
import com.adamkapus.hikingapp.domain.interactor.map.FlowerLocationInteractor
import com.adamkapus.hikingapp.domain.interactor.route.RouteInteractor
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResult
import com.adamkapus.hikingapp.domain.model.map.Route
import com.adamkapus.hikingapp.ui.map.MapUiState.Initial
import com.adamkapus.hikingapp.ui.map.MapUiState.RouteLoaded
import com.adamkapus.hikingapp.ui.map.model.FlowerOnMap
import com.adamkapus.hikingapp.ui.map.model.toFlowerOnMap
import com.adamkapus.hikingapp.utils.FlowerRarity
import com.adamkapus.hikingapp.utils.FlowerResolver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val flowerResolver: FlowerResolver,
    private val locationInteractor: LocationInteractor,
    private val flowerLocationInteractor: FlowerLocationInteractor,
    private val routeInteractor: RouteInteractor
) : ViewModel() {
    private val userPosition = MutableStateFlow<Location?>(null)
    private val flowerVisibilityState = MutableStateFlow(
        FlowerVisibiltyState(
            emptySet()
        )
    )
    private val allFlowers = MutableStateFlow<List<FlowerOnMap>>(listOf())
    /*private val visibleFlowerLocations = combine(
        flowerVisibilityState,
        allFlowerLocations
    ) { flowerVisibilityState, allFlowerLocations ->
        allFlowerLocations.filter {
            if (it.rarity == null) {
                false
            } else {
                flowerVisibilityState.visibleCategories.contains(FlowerRarity.valueOf(it.rarity))
            }
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), initialValue = listOf()
    )*/

    private val route = MutableStateFlow<Route?>(null)


    val uiState = combine(
        userPosition,
        flowerVisibilityState,
        allFlowers,
        route
    ) { userPosition, flowerVisibilityState, allFlowers, route ->
        val visibleFlowers = allFlowers.filter {
            if (it.rarity == null) {
                false
            } else {
                flowerVisibilityState.visibleCategories.contains(it.rarity)
            }
        }
        if (route != null) {
            RouteLoaded(
                flowerVisibilityState = flowerVisibilityState,
                flowers = visibleFlowers,
                route = route
            )
        } else {
            Initial(
                flowerVisibilityState = flowerVisibilityState,
                flowers = visibleFlowers,
                userPosition = userPosition
            )
        }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), initialValue = Initial(
            flowerVisibilityState = FlowerVisibiltyState(emptySet()),
            flowers = listOf(),
            null
        )
    )

    private val _locationPermissionsDeniedEvent = MutableStateFlow(false)
    val locationPermissionsDeniedEvent = _locationPermissionsDeniedEvent.asStateFlow()

    //fun on

    fun loadUserPosition() = viewModelScope.launch {
        Log.d("PLS", "loaduserpositions")
        val resp = locationInteractor.getLastLocation()
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

    fun loadFlowerLocations() = viewModelScope.launch {
        val resp = flowerLocationInteractor.getFlowerLocations()
        when (resp) {
            is InteractorError -> {//Todo Event post?
            }
            is InteractorResult -> {
                val flowers = resp.result.map { it.toFlowerOnMap(flowerResolver) }
                allFlowers.update { flowers }
            }
        }
    }

    fun onRarityVisibilityChanged(rarity: FlowerRarity, isVisible: Boolean) {
        val raritySet = flowerVisibilityState.value.visibleCategories.toMutableSet()
        if (isVisible) {
            raritySet.add(rarity)
        } else {
            raritySet.remove(rarity)
        }
        flowerVisibilityState.update {
            FlowerVisibiltyState(raritySet)
        }
    }

    /*
    fun onFlowerLocationStateChanged(visibleCategories: Set<FlowerRarity>) {
        flowerVisibilityState.update { FlowerVisibiltyState(visibleCategories) }
    }
    */
    fun loadRouteFromGpxFile(inputStream: InputStream) = viewModelScope.launch {
        val inter = GpxInteractor()
        val resp = inter.parseGpxFile(inputStream)
        route.update { Route(null, "nevecske", resp) }
    }

    fun loadUserRecordedRoute(id: Int) = viewModelScope.launch {
        val resp = routeInteractor.getRoute(id)
        when (resp) {
            is InteractorError -> {}
            is InteractorResult -> {
                route.update { resp.result }
            }
        }
    }

    //ToDo
    fun onLocationPermissionsDenied() {

    }

    //ToDo
    fun handledLocationPermissionsDenied() {

    }

}