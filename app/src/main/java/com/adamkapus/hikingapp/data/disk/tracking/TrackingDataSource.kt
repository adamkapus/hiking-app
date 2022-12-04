package com.adamkapus.hikingapp.data.disk.tracking

import android.content.SharedPreferences
import com.adamkapus.hikingapp.data.model.DataSourceResponse
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.adamkapus.hikingapp.data.model.toTrackedLocation
import com.adamkapus.hikingapp.data.model.toTrackedLocationItem
import com.adamkapus.hikingapp.domain.model.tracking.TrackedLocation
import javax.inject.Inject

class TrackingDataSource @Inject constructor(
    private val trackingDao: TrackingDao,
    private val sharedPreferences: SharedPreferences
) {

    suspend fun insertTrackedLocation(newLocation: TrackedLocation) {
        trackingDao.insertTrackedLocationItem(newLocation.toTrackedLocationItem())
    }

    suspend fun getTrackedLocations(): List<TrackedLocation> {
        return trackingDao.getAllTrackedLocationItem().map { it.toTrackedLocation() }
    }

    suspend fun deleteTrackedLocations() {
        trackingDao.deleteAllTrackedLocationItem()
    }


    suspend fun startTracking() {
        with(sharedPreferences.edit()) {
            putBoolean(trackingInProgressKey, true)
            apply()
        }
    }

    suspend fun stopTracking() {
        with(sharedPreferences.edit()) {
            putBoolean(trackingInProgressKey, false)
            apply()
        }
    }

    suspend fun isTrackingInProgress(): DataSourceResponse<Boolean> {
        val isTrackingInProgress = sharedPreferences.getBoolean(trackingInProgressKey, false)
        return DataSourceResult(isTrackingInProgress)
    }

    companion object {
        private const val trackingInProgressKey: String = "TRACKING_IN_PROGRESS"
    }
}