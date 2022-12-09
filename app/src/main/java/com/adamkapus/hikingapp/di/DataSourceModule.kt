package com.adamkapus.hikingapp.di

import android.content.SharedPreferences
import com.adamkapus.hikingapp.data.disk.route.CoordinateDao
import com.adamkapus.hikingapp.data.disk.route.RouteDao
import com.adamkapus.hikingapp.data.disk.route.RouteDataSource
import com.adamkapus.hikingapp.data.disk.tracking.TrackingDao
import com.adamkapus.hikingapp.data.disk.tracking.TrackingDataSource
import com.adamkapus.hikingapp.data.gpx.GpxDataSource
import com.adamkapus.hikingapp.data.location.LocationDataSource
import com.adamkapus.hikingapp.data.network.AuthenticationDataSource
import com.adamkapus.hikingapp.data.network.FlowerImageDataSource
import com.adamkapus.hikingapp.data.network.FlowerLocationDataSource
import com.adamkapus.hikingapp.utils.FlowerResolver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ticofab.androidgpxparser.parser.GPXParser

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun getFlowerLocationDataSource(
        flowerResolver: FlowerResolver,
        db: FirebaseFirestore
    ): FlowerLocationDataSource {
        return FlowerLocationDataSource(flowerResolver, db)
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
    fun getAuthenticationDataSource(auth: FirebaseAuth): AuthenticationDataSource {
        return AuthenticationDataSource(auth)
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

    @Provides
    fun getGpxDataSource(parser: GPXParser): GpxDataSource {
        return GpxDataSource(parser)
    }


}