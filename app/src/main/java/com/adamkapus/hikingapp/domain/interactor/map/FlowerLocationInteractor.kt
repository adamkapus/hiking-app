package com.adamkapus.hikingapp.domain.interactor.map

import android.util.Log
import com.adamkapus.hikingapp.data.model.FlowerLocation
import com.adamkapus.hikingapp.data.network.FlowerLocationDataSource
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.toInteractorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FlowerLocationInteractor @Inject constructor(
    private val flowerLocationDataSource: FlowerLocationDataSource
) {

    suspend fun getFlowerLocations(): InteractorResponse<List<FlowerLocation>> =
        withContext(Dispatchers.IO) {
            Log.d("PLS", "GETFLOWERLOC")
            val resp = flowerLocationDataSource.getFlowerLocations()
            Log.d("PLS", "INT RESP:" + resp.toString())
            return@withContext resp.toInteractorResponse()
        }
}