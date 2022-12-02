package com.adamkapus.hikingapp.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LocationModule {


    /*@Provides
    @Singleton
    fun getFusedLocationProviderClient(@ApplicationContext appContext: Context): FusedLocationProviderClient {
        return FusedLocationProviderClient(appContext)
    }*/
}