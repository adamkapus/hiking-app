package com.adamkapus.hikingapp.data.disk.tracking

import com.adamkapus.hikingapp.data.model.toTrackedLocation
import com.adamkapus.hikingapp.data.model.toTrackedLocationItem
import com.adamkapus.hikingapp.domain.model.tracking.TrackedLocation
import javax.inject.Inject

class TrackingDataSource @Inject constructor(
    private val trackingDao: TrackingDao
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
}