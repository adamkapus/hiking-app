package com.adamkapus.hikingapp.domain.interactor.camera

import android.graphics.Bitmap
import android.util.Log
import com.adamkapus.hikingapp.data.location.LocationDataSource
import com.adamkapus.hikingapp.data.model.DataSourceError
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.adamkapus.hikingapp.data.network.FlowerImageDataSource
import com.adamkapus.hikingapp.data.network.FlowerLocationDataSource
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.toInteractorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FlowerImageSubmissionInteractor @Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val flowerImageDataSource: FlowerImageDataSource,
    private val flowerLocationDataSource: FlowerLocationDataSource
) {


    suspend fun submitFlowerImageWithLocation(
        flowerName: String,
        image: Bitmap?
    ): InteractorResponse<Unit> =
        withContext(Dispatchers.IO) {
            val lastLoc = locationDataSource.getLastPosition()
            if (lastLoc is DataSourceError) {
                Log.d("PLS", "NINCS LOKACIO!!")
                return@withContext InteractorError
            }
            val location = (lastLoc as DataSourceResult).result
            val latitude = location?.latitude
            val longitude = location?.longitude
            if (latitude == null || longitude == null) {
                Log.d("PLS", "NINCS LOKACIO mert null!!")
                return@withContext InteractorError
            }


            if (image == null) {
                Log.d("PLS", "bitmap null")
                return@withContext InteractorError
            }
            Log.d("PLS", "Interactorban es nem null")
            val imageUploadResponse = flowerImageDataSource.submitFlowerImage(image)
            if (imageUploadResponse is DataSourceError) {
                return@withContext InteractorError
            }

            val imageURL = (imageUploadResponse as DataSourceResult).result.toString()

            val resp = flowerLocationDataSource.submitFlowerLocation(
                name = flowerName,
                Lat = latitude,
                Lng = longitude,
                imageURL = imageURL
            )

            return@withContext resp.toInteractorResponse()
        }


}