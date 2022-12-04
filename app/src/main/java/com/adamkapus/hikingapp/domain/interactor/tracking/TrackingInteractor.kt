package com.adamkapus.hikingapp.domain.interactor.tracking

import android.location.Location
import android.util.Log
import com.adamkapus.hikingapp.data.disk.route.RouteDataSource
import com.adamkapus.hikingapp.data.disk.tracking.TrackingDataSource
import com.adamkapus.hikingapp.data.model.DataSourceError
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.InteractorResult
import com.adamkapus.hikingapp.domain.model.map.Coordinate
import com.adamkapus.hikingapp.domain.model.map.Route
import com.adamkapus.hikingapp.domain.model.toInteractorResponse
import com.adamkapus.hikingapp.domain.model.tracking.TrackedLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrackingInteractor @Inject constructor(
    private val trackingDataSource: TrackingDataSource,
    private val routeDataSource: RouteDataSource
) {

    suspend fun startTracking() = withContext(Dispatchers.IO) {
        trackingDataSource.startTracking()
    }

    suspend fun stopTracking() = withContext(Dispatchers.IO) {
        trackingDataSource.stopTracking()
    }

    suspend fun isTrackingInProgress(): InteractorResponse<Boolean> = withContext(Dispatchers.IO) {
        val resp = trackingDataSource.isTrackingInProgress()
        when (resp) {
            is DataSourceError -> {
                InteractorResult(false)
            }
            is DataSourceResult -> {
                resp.toInteractorResponse()
            }
        }

    }

    suspend fun addTrackedLocation(location: Location) = withContext(Dispatchers.IO) {
        val latitude = location.latitude
        val longitude = location.longitude
        trackingDataSource.insertTrackedLocation(
            TrackedLocation(
                latitude = latitude,
                longitude = longitude
            )
        )
    }

    /*suspend fun getTrackedLocations() : DataList<TrackedLocation> = withContext(Dispatchers.IO) {
        trackingDataSource.getTrackedLocations()
    }*/

    suspend fun saveRoute(): InteractorResponse<Unit> =
        withContext(Dispatchers.IO) {
            val trackedLocations = trackingDataSource.getTrackedLocations()
            val coordinateList = trackedLocations.map { Coordinate(it.latitude, it.longitude) }
            val r = Route(id = null, name = "nev", points = coordinateList)
            Log.d("PLS", r.toString())
            routeDataSource.insertRoute(r)
            InteractorResult(Unit)
        }
}