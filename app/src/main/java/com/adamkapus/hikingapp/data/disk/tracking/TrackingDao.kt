package com.adamkapus.hikingapp.data.disk.tracking

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.adamkapus.hikingapp.data.model.TrackedLocationItem

@Dao
interface TrackingDao {

    @Insert
    suspend fun insertTrackedLocationItem(trackedLocationItem: TrackedLocationItem)

    @Query("SELECT * FROM trackedlocationitem")
    suspend fun getAllTrackedLocationItem(): List<TrackedLocationItem>

    @Query("DELETE FROM trackedlocationitem")
    suspend fun deleteAllTrackedLocationItem()
}