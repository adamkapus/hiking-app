package com.adamkapus.hikingapp.domain.interactor.location

import android.location.Location
import com.adamkapus.hikingapp.data.location.LocationDataSource
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.toInteractorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationInteractor @Inject constructor(
    private val locationDataSource: LocationDataSource
) {
    suspend fun getLastLocation(): InteractorResponse<Location?> =
        withContext(Dispatchers.IO) {
            val response = locationDataSource.getLastPosition()
            response.toInteractorResponse()
        }
}