package com.adamkapus.hikingapp.di

import android.content.SharedPreferences
import com.adamkapus.hikingapp.data.disk.route.CoordinateDao
import com.adamkapus.hikingapp.data.disk.route.RouteDao
import com.adamkapus.hikingapp.data.disk.route.RouteDataSource
import com.adamkapus.hikingapp.data.disk.tracking.TrackingDao
import com.adamkapus.hikingapp.data.disk.tracking.TrackingDataSource
import com.adamkapus.hikingapp.data.location.LocationDataSource
import com.adamkapus.hikingapp.data.network.AuthenticationDataSource
import com.adamkapus.hikingapp.data.network.FlowerImageDataSource
import com.adamkapus.hikingapp.data.network.FlowerLocationDataSource
import com.adamkapus.hikingapp.utils.FlowerResolver
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun getFlowerLocationDataSource(flowerResolver: FlowerResolver): FlowerLocationDataSource {
        return FlowerLocationDataSource(flowerResolver)
    }

    @Provides
    fun getLocationDataSource(fusedLocationProviderClient: FusedLocationProviderClient): LocationDataSource {
        return LocationDataSource(fusedLocationProviderClient)
    }

    @Provides
    fun getFlowerImageDataSource(): FlowerImageDataSource {
        return FlowerImageDataSource()
    }

    @Provides
    fun getAuthenticationDataSource(): AuthenticationDataSource {
        return AuthenticationDataSource()
    }

    @Provides
    fun getTrackingDataSource(
        trackingDao: TrackingDao,
        sharedPreferences: SharedPreferences
    ): TrackingDataSource {
        return TrackingDataSource(trackingDao, sharedPreferences)
    }

    @Provides
    fun getRouteDataSource(routeDao: RouteDao, coordinateDao: CoordinateDao): RouteDataSource {
        return RouteDataSource(routeDao, coordinateDao)
    }


}