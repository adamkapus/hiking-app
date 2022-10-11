package com.adamkapus.hikingapp.trackscreen.tracking

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval: Long): Flow<Location>
    fun stopLocationMonitoring()

    class LocationException(message: String) : Exception()
}