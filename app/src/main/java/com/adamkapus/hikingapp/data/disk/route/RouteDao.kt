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
        "SELECT * FROM routeitem" + " JOIN coordinateitem ON routeitem.id = coordinateitem.route_id"
    )
    suspend fun getAllRouteItem(): Map<RouteItem, List<CoordinateItem>>

    /*@Query(
        "SELECT * FROM routeitem" + " JOIN coordinateitem ON routeitem.id = coordinateitem.route_id WHERE routeitem.id = :id"
    )
    suspend fun getRouteItem(id : Int): Map<RouteItem, List<CoordinateItem>>*/

    @Query("DELETE FROM routeitem")
    suspend fun deleteAllRouteItem()
}