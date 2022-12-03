package com.adamkapus.hikingapp.di

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
object DataModule {

    @Provides
    fun getFlowerLocationDataSource(flowerResolver: FlowerResolver): FlowerLocationDataSource {
        return FlowerLocationDataSource(flowerResolver)
    }

    @Provides
    fun getLocationDataSource(fusedLocationProviderClient: FusedLocationProviderClient): LocationDataSource {
        return LocationDataSource(fusedLocationProviderClient)
    }

    @Provides
    fun getFlowerImageDataSource(flowerResolver: FlowerResolver): FlowerImageDataSource {
        return FlowerImageDataSource()
    }

    @Provides
    fun getAuthenticationDataSource(): AuthenticationDataSource {
        return AuthenticationDataSource()
    }


}