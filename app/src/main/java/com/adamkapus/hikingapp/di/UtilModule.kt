package com.adamkapus.hikingapp.di

import android.content.Context
import com.adamkapus.hikingapp.utils.FlowerResolver
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ticofab.androidgpxparser.parser.GPXParser
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    @Singleton
    fun getFlowerResolver(@ApplicationContext appContext: Context): FlowerResolver {
        return FlowerResolver(appContext)
    }

    @Provides
    fun getFusedLocationProviderClient(@ApplicationContext appContext: Context): FusedLocationProviderClient {
        return FusedLocationProviderClient(appContext)
    }

    @Provides
    @Singleton
    fun getGPXParser(): GPXParser {
        return GPXParser()
    }

    @Provides
    @Singleton
    fun getFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun getFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }
}