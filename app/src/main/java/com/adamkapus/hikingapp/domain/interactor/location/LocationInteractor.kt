package com.adamkapus.hikingapp.domain.interactor.location

import android.location.Location
import com.adamkapus.hikingapp.data.LocationDataSource
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.toInteractorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationInteractor {

    suspend fun getLastLocation(): InteractorResponse<Location?> =
        withContext(Dispatchers.IO) {
            val dataSource = LocationDataSource()
            val response = dataSource.getLastPosition()
            response.toInteractorResponse()
        }
}