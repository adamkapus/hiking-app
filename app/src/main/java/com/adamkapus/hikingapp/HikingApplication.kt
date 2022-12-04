package com.adamkapus.hikingapp

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HikingApplication : Application() {

    companion object {

        const val NOTIFICATION_CHANNEL_ID = "location"

    }

    @SuppressLint("VisibleForTests")
    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /*@SuppressLint("VisibleForTests")
    fun getFusedLocationProviderClient(): FusedLocationProviderClient {
        return FusedLocationProviderClient(this)
    }*/
}