package com.adamkapus.hikingapp.data.disk

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adamkapus.hikingapp.data.disk.route.CoordinateDao
import com.adamkapus.hikingapp.data.disk.route.RouteDao
import com.adamkapus.hikingapp.data.disk.tracking.TrackingDao
import com.adamkapus.hikingapp.data.model.CoordinateItem
import com.adamkapus.hikingapp.data.model.RouteItem
import com.adamkapus.hikingapp.data.model.TrackedLocationItem

@Database(
    version = 1,
    exportSchema = false,
    entities = [TrackedLocationItem::class, RouteItem::class, CoordinateItem::class]
)
abstract class HikingDatabase : RoomDatabase() {
    abstract fun trackingDao(): TrackingDao
    abstract fun routeDao(): RouteDao
    abstract fun coordinateDao(): CoordinateDao
}
