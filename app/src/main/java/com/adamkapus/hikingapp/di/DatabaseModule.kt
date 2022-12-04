package com.adamkapus.hikingapp.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.room.Room
import com.adamkapus.hikingapp.data.disk.route.CoordinateDao
import com.adamkapus.hikingapp.data.disk.route.RouteDao
import com.adamkapus.hikingapp.data.disk.tracking.TrackingDao
import com.adamkapus.hikingapp.data.disk.HikingDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideTrackingDao(database: HikingDatabase): TrackingDao {
        return database.trackingDao()
    }

    @Provides
    fun provideRouteDao(database: HikingDatabase): RouteDao {
        return database.routeDao()
    }

    @Provides
    fun provideCoordinateDao(database: HikingDatabase): CoordinateDao {
        return database.coordinateDao()
    }

    @Provides
    @Singleton
    fun provideHikingDatabase(@ApplicationContext appContext: Context): HikingDatabase {
        return Room.databaseBuilder(
            appContext,
            HikingDatabase::class.java,
            "hiking_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun getSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return getDefaultSharedPreferences(appContext)
    }
}