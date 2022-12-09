package com.adamkapus.hikingapp.domain.interactor.gpx

import com.adamkapus.hikingapp.data.gpx.GpxDataSource
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.map.Coordinate
import com.adamkapus.hikingapp.domain.model.toInteractorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject

const val TAG = "GPX"

class GpxInteractor @Inject constructor(
    private val gpxDataSource: GpxDataSource
) {

    suspend fun parseGpxFile(inputStream: InputStream): InteractorResponse<MutableList<Coordinate>> =
        withContext(Dispatchers.IO) {
            return@withContext gpxDataSource.parseGpxFile(inputStream).toInteractorResponse()
        }

}