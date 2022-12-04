package com.adamkapus.hikingapp.domain.interactor.tracking

import com.adamkapus.hikingapp.data.disk.tracking.TrackingDataSource
import javax.inject.Inject

class TrackingInteractor @Inject constructor(
    private val trackingDataSource: TrackingDataSource
) {

    suspend fun startTracking(){

    }

    suspend fun stopTracking(){

    }

    suspend fun isTrackingInProgress(){

    }

    suspend fun addTrackedLocation(){

    }

    suspend fun getTrackedLocations(){

    }

    suspend fun calculateRouteFromLocations(){

    }
}