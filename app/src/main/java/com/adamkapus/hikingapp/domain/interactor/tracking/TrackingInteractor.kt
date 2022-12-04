package com.adamkapus.hikingapp.domain.interactor.tracking

import com.adamkapus.hikingapp.data.disk.tracking.TrackingDataSource
import com.adamkapus.hikingapp.data.model.DataSourceError
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.InteractorResult
import com.adamkapus.hikingapp.domain.model.toInteractorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrackingInteractor @Inject constructor(
    private val trackingDataSource: TrackingDataSource
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
                return@withContext InteractorResult(false)
            }
            is DataSourceResult -> {
                return@withContext resp.toInteractorResponse()
            }
        }

    }

    suspend fun addTrackedLocation() = withContext(Dispatchers.IO) {

    }

    suspend fun getTrackedLocations() = withContext(Dispatchers.IO) {

    }

    suspend fun calculateRouteFromLocations() = withContext(Dispatchers.IO) {

    }
}