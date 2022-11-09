package com.adamkapus.hikingapp.ui.track.tracking

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adamkapus.hikingapp.HikingApplication.Companion.NOTIFICATION_CHANNEL_ID
import com.adamkapus.hikingapp.R
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    private val _locationList = MutableLiveData<List<Location>>()
    val locationList: LiveData<List<Location>> = _locationList

    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        // Return this instance of LocationService so clients can call public methods
        fun getService(): LocationService = this@LocationService
    }

    override fun onBind(intent: Intent): IBinder {
        Toast.makeText(this, "binded", Toast.LENGTH_SHORT).show()
        Log.d("PLS", "onBind")
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Toast.makeText(this, "unbinded", Toast.LENGTH_SHORT).show()
        return super.onUnbind(intent)
    }


    private fun start() {
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(2000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val newList =
                    _locationList.value?.toMutableList()//?.add(location) ?: mutableListOf(location)
                newList?.add(location)
                if (newList != null) {
                    _locationList.postValue(newList!!)
                } else {
                    _locationList.postValue(mutableListOf(location))
                }
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                notificationManager.notify(1, updatedNotification.build())
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        Toast.makeText(this, "stop meghivva", Toast.LENGTH_SHORT).show()
        stopForeground(true)
        locationClient.stopLocationMonitoring()
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "service destroyed", Toast.LENGTH_SHORT).show()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}