package com.adamkapus.hikingapp.domain.interactor.camera

import android.graphics.Bitmap
import android.util.Log
import com.adamkapus.hikingapp.data.LocationDataSource
import com.adamkapus.hikingapp.data.model.DataSourceError
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.adamkapus.hikingapp.data.network.FlowerImageDataSource
import com.adamkapus.hikingapp.domain.model.InteractorError
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.InteractorResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FlowerImageSubmissionInteractor {


    suspend fun submitFlowerImageWithLocation(bitmap: Bitmap?): InteractorResponse<Unit> = withContext(Dispatchers.IO) {
        /*val locSource = LocationDataSource()
        val lastLoc = locSource.getLastPosition()
        if(lastLoc is DataSourceError){
            return@withContext InteractorError
        }*/
        //val pos = (lastLoc as DataSourceResult).result?.latitude


        if (bitmap == null) {
            Log.d("PLS", "bitmap null")
            return@withContext InteractorError
        }
        Log.d("PLS", "Interactorban es nem null" )
        val datasource = FlowerImageDataSource()
        val imageUploadResponse = datasource.submitFlowerImage(bitmap)
        if (imageUploadResponse is DataSourceError) {
            return@withContext InteractorError
        }

        Log.d("PLS", "Interactorresultig jutott")
        return@withContext InteractorResult(Unit)


    }


}