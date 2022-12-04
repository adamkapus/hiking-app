package com.adamkapus.hikingapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.adamkapus.hikingapp.domain.model.tracking.TrackedLocation

@Entity(tableName = "trackedlocationitem")
data class TrackedLocationItem(
    @PrimaryKey(autoGenerate = true) val id: Double?,
    val latitude: Double,
    val longitude: Double
)

fun TrackedLocationItem.toTrackedLocation(): TrackedLocation {
    return TrackedLocation(
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun TrackedLocation.toTrackedLocationItem(): TrackedLocationItem {
    return TrackedLocationItem(
        id = null,
        latitude = this.latitude,
        longitude = this.longitude
    )
}


