package com.adamkapus.hikingapp.data.disk.route

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.adamkapus.hikingapp.data.model.CoordinateItem

@Dao
interface CoordinateDao {

    @Insert
    suspend fun insertCoordinateItem(coordinateItem: CoordinateItem)

    @Query(
        "SELECT * FROM coordinateitem WHERE coordinateitem.route_id = :routeId"
    )
    suspend fun getRoutesCoordinateItem(routeId: Int): List<CoordinateItem>

}