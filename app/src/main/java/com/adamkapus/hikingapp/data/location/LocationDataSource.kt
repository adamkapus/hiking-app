package com.adamkapus.hikingapp.data.location

import android.annotation.SuppressLint
import android.location.Location
import com.adamkapus.hikingapp.HikingApplication
import com.adamkapus.hikingapp.HikingApplication.Companion.fusedLocationProviderClient
import com.adamkapus.hikingapp.data.model.DataSourceResponse
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class LocationDataSource @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

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