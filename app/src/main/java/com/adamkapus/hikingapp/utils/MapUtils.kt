package com.adamkapus.hikingapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.Location
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.adamkapus.hikingapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine


object MapUtils {
    /*fun assembleRoute(points: List<LatLng>) {
        val route: Polyline = map.addPolyline(
            PolylineOptions()
                .width(_strokeWidth)
                .color(_pathColor)
                .geodesic(true)
                .zIndex(z)
        )
        route.points = routePoints
    }*/
    fun addRouteToMap(context: Context, map: GoogleMap, trackingPointList: List<LatLng>) {
        val outerPolyline: Polyline = map.addPolyline(
            createOuterPolyLineOptions(context)
        )
        outerPolyline.points = trackingPointList

        val innerPolyline: Polyline = map.addPolyline(
            createInnerPolyLineOptions(context)
        )
        innerPolyline.points = trackingPointList


    }

    private fun createOuterPolyLineOptions(context: Context): PolylineOptions {
        return PolylineOptions().color(Color.BLUE).width(14.0f)
            .jointType(JointType.ROUND)//.pattern(pattern)
    }

    private fun createInnerPolyLineOptions(context: Context): PolylineOptions {
        /*val pattern = listOf(
            Dot(), Gap(20F), Dash(30F), Gap(20F)
        )*/


        val greenStarBitmap =
            AppCompatResources.getDrawable(context, R.drawable.ic_green_star)!!.toBitmap()
        val yellowStarBitmap =
            AppCompatResources.getDrawable(context, R.drawable.ic_yellow_star)!!.toBitmap()

        return PolylineOptions().startCap(
            CustomCap(BitmapDescriptorFactory.fromBitmap(greenStarBitmap), 10f)
        ).endCap(
            CustomCap(BitmapDescriptorFactory.fromBitmap(yellowStarBitmap), 10f)
        ).color(Color.CYAN).width(12.0f).jointType(JointType.ROUND)//.pattern(pattern)
    }
}

/*
class Route(trackingPoints: List<LatLng>) {
    val startPoint = trackingPoints.first()
    val endPoint = trackingPoints.last()
}*/

/*
data class trackingPoint(
    val Lat : Float,
    val Lng : Float
)*/

// Extension function on FusedLocationProviderClient, returns last known location
/*@SuppressLint("MissingPermission")
suspend fun FusedLocationProviderClient.awaitLastLocation(): Location =

    // Create a new coroutine that can be cancelled
    suspendCancellableCoroutine<Location> { continuation ->

        // Add listeners that will resume the execution of this coroutine
        getLastLocation().addOnSuccessListener { location ->
            // Resume coroutine and return location
            continuation.resume(location) {}
        }.addOnFailureListener { e ->
            // Resume the coroutine by throwing an exception
            continuation.resume(null)
        }

        // End of the suspendCancellableCoroutine block. This suspends the
        // coroutine until one of the callbacks calls the continuation parameter.
    }*/
