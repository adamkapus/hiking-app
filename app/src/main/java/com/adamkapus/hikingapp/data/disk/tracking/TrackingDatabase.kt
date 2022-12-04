package com.adamkapus.hikingapp.data.disk.tracking

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adamkapus.hikingapp.data.model.TrackedLocationItem

@Database(
    version = 1,
    exportSchema = false,
    entities = [TrackedLocationItem::class]
)
abstract class TrackingDatabase : RoomDatabase() {
    abstract fun trackingDao(): TrackingDao
}


/*
* @Database(
    version = 1,
    exportSchema = false,
    entities = [News::class]
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}
* */