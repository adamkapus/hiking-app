package com.adamkapus.hikingapp.data.network

import com.adamkapus.hikingapp.data.model.DataSourceError
import com.adamkapus.hikingapp.data.model.DataSourceResponse
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.adamkapus.hikingapp.data.model.FlowerLocation
import com.adamkapus.hikingapp.utils.FlowerResolver
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObjects
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FlowerLocationDataSource @Inject constructor(
    private val flowerResolver: FlowerResolver,
    private val db: FirebaseFirestore
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
        return suspendCoroutine { continuation ->
            db.collection("flowers")
                .get(Source.DEFAULT)
                .addOnSuccessListener { documents ->
                    continuation.resume(
                        DataSourceResult(documents.toObjects<FlowerLocation>())
                    )
                }
                .addOnFailureListener { continuation.resume(DataSourceError) }
        }
    }
}