package com.adamkapus.hikingapp.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.adamkapus.hikingapp.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*


object MapUtils {
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
            .jointType(JointType.ROUND)
    }

    private fun createInnerPolyLineOptions(context: Context): PolylineOptions {
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

