package com.adamkapus.hikingapp.trackscreen

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class TrackScreenViewModel : ViewModel() {

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
    }
}