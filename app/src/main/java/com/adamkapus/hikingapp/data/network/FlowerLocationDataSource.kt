package com.adamkapus.hikingapp.data.network

import android.util.Log
import com.adamkapus.hikingapp.data.model.DataSourceError
import com.adamkapus.hikingapp.data.model.DataSourceResponse
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.adamkapus.hikingapp.data.model.FlowerLocation
import com.adamkapus.hikingapp.utils.FlowerResolver
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FlowerLocationDataSource @Inject constructor(
    private val flowerResolver: FlowerResolver
) {

    suspend fun submitFlowerLocation(
        name: String,
        Lat: Double,
        Lng: Double,
        imageURL: String
    ): DataSourceResponse<Unit> {
        val data = hashMapOf(
            "name" to name,
            "Lat" to Lat,
            "Lng" to Lng,
            "imageUrl" to imageURL,
            "rarity" to flowerResolver.getRarity(name).toString()
        )
        val db: FirebaseFirestore = Firebase.firestore
        return suspendCoroutine { continuation ->
            db.collection("flowers")
                .add(data)
                .addOnSuccessListener {
                    continuation.resume(DataSourceResult(Unit))
                }
                .addOnFailureListener {
                    continuation.resume(DataSourceError)
                }
        }
    }

    suspend fun getFlowerLocations(): DataSourceResponse<List<FlowerLocation>> {
        Log.d("PLS", "DS-BEN MAR")
        val db: FirebaseFirestore = Firebase.firestore
        return suspendCoroutine { continuation ->
            db.collection("flowers")
                .get()
                .addOnSuccessListener { documents ->
                    continuation.resume(DataSourceResult(documents.toObjects<FlowerLocation>()))
                }
                .addOnFailureListener { continuation.resume(DataSourceError) }
        }
    }
}