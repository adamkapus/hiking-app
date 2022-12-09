package com.adamkapus.hikingapp.data.disk.route

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.adamkapus.hikingapp.data.model.CoordinateItem
import com.adamkapus.hikingapp.data.model.RouteItem

@Dao
interface RouteDao {

    @Insert
    suspend fun insertRouteItem(routeItem: RouteItem): Long

    @Query(
        "SELECT * FROM routeitem"
    )
    suspend fun getAllRouteItem(): List<RouteItem>

    @Query(
        "SELECT * FROM routeitem WHERE routeitem.id = :id"
    )
    suspend fun getRouteItem(id: Int): RouteItem

    @Query("DELETE FROM routeitem")
    suspend fun deleteAllRouteItem()
}