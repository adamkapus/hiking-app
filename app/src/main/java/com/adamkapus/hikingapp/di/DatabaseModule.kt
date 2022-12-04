package com.adamkapus.hikingapp.di

import android.content.Context
import androidx.room.Room
import com.adamkapus.hikingapp.data.disk.tracking.TrackingDao
import com.adamkapus.hikingapp.data.disk.tracking.TrackingDatabase
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
    fun provideTrackingDao(database: TrackingDatabase): TrackingDao {
        return database.trackingDao()
    }

    @Provides
    @Singleton
    fun provideTrackingDatabase(@ApplicationContext appContext: Context): TrackingDatabase {
        return Room.databaseBuilder(
            appContext,
            TrackingDatabase::class.java,
            "tracking_database"
        ).fallbackToDestructiveMigration().build()
    }
}