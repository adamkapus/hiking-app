package com.adamkapus.hikingapp.data

import android.annotation.SuppressLint
import android.location.Location
import com.adamkapus.hikingapp.HikingApplication
import com.adamkapus.hikingapp.data.model.DataSourceResponse
import com.adamkapus.hikingapp.data.model.DataSourceResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class LocationDataSource {

    private val fusedLocationProviderClient = HikingApplication.fusedLocationProviderClient


    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    suspend fun getLastPosition() =
        suspendCancellableCoroutine<DataSourceResponse<Location?>> { continuation ->
            fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        continuation.resume(DataSourceResult(result = task.result)) {}
                    } else {
                        continuation.resume(DataSourceResult(result = null)) {}
                    }

                }
                .addOnFailureListener {
                    continuation.resume(DataSourceResult(result = null)) {}
                }

        }

}