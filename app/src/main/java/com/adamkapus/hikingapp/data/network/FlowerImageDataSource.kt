package com.adamkapus.hikingapp.data.network

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.adamkapus.hikingapp.data.model.DataSourceError
import com.adamkapus.hikingapp.data.model.DataSourceResponse
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FlowerImageDataSource {


    suspend fun submitFlowerImage(bitmap: Bitmap): DataSourceResponse<Uri> {
        Log.d("PLS", "ds-ben")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
        val imageInBytes = baos.toByteArray()
        Log.d("PLS", "ds-ben kep kesz")

        val storageReference = FirebaseStorage.getInstance().reference
        val newImageName = withContext(Dispatchers.IO) {
            URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8")
        } + ".jpg"
        Log.d("PLS", "ds-ben encode ok")
        val newImageRef = storageReference.child("images/$newImageName")

        return suspendCancellableCoroutine { continuation ->
            newImageRef.putBytes(imageInBytes)
                .addOnFailureListener {
                    Log.d("PLS", "ds-ben onfail")
                    continuation.resume(DataSourceError) }
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        continuation.resume(DataSourceError)
                        Log.d("PLS", task.exception.toString())
                    }
                    newImageRef.downloadUrl
                }
                .addOnSuccessListener { downloadUri ->
                    Log.d("PLS", "URI " + downloadUri.toString())
                    continuation.resume(DataSourceResult(downloadUri))
                }
        }

    }

}