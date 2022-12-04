package com.adamkapus.hikingapp.di

import com.adamkapus.hikingapp.data.disk.route.RouteDataSource
import com.adamkapus.hikingapp.data.disk.tracking.TrackingDataSource
import com.adamkapus.hikingapp.data.location.LocationDataSource
import com.adamkapus.hikingapp.data.network.FlowerImageDataSource
import com.adamkapus.hikingapp.data.network.FlowerLocationDataSource
import com.adamkapus.hikingapp.domain.interactor.analysis.AnalysisInteractor
import com.adamkapus.hikingapp.domain.interactor.authentication.AuthenticationInteractor
import com.adamkapus.hikingapp.domain.interactor.camera.FlowerImageSubmissionInteractor
import com.adamkapus.hikingapp.domain.interactor.gpx.GpxInteractor
import com.adamkapus.hikingapp.domain.interactor.location.LocationInteractor
import com.adamkapus.hikingapp.domain.interactor.map.FlowerLocationInteractor
import com.adamkapus.hikingapp.domain.interactor.tracking.TrackingInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InteractorModule {

    @Singleton
    @Provides
    fun getAuthenticationInteractor(): AuthenticationInteractor {
        return AuthenticationInteractor()
    }

    @Singleton
    @Provides
    fun getAnalysisInteractor(): AnalysisInteractor {
        return AnalysisInteractor()
    }

    @Singleton
    @Provides
    fun getFlowerImageSubmissionInteractor(
        locationDataSource: LocationDataSource,
        flowerImageDataSource: FlowerImageDataSource,
        flowerLocationDataSource: FlowerLocationDataSource
    ): FlowerImageSubmissionInteractor {
        return FlowerImageSubmissionInteractor(
            locationDataSource, flowerImageDataSource, flowerLocationDataSource
        )
    }

    @Singleton
    @Provides
    fun getGpxInteractor(): GpxInteractor {
        return GpxInteractor()
    }

    @Singleton
    @Provides
    fun getLocationInteractor(
        locationDataSource: LocationDataSource
    ): LocationInteractor {
        return LocationInteractor(locationDataSource)
    }

    @Singleton
    @Provides
    fun getFlowerLocationInteractor(flowerLocationDataSource: FlowerLocationDataSource): FlowerLocationInteractor {
        return FlowerLocationInteractor(flowerLocationDataSource)
    }

    @Singleton
    @Provides
    fun getTrackingInteractor(trackingDataSource: TrackingDataSource, routeDataSource: RouteDataSource): TrackingInteractor {
        return TrackingInteractor(trackingDataSource, routeDataSource)
    }
}