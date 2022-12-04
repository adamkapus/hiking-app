package com.adamkapus.hikingapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "routeitem")
data class RouteItem(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
)

@Entity(tableName = "coordinateitem")
data class CoordinateItem(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val route_id: Long,
    val lat: Double,
    val lng: Double
)

/*
fun RouteItem.toRoute(): Route {
    return Route(
        id = this.id,
        name = this.name,
        points = listOf()
    )
}

fun Route.toRouteItem(): RouteItem {
    return RouteItem(
        id = this.id,
        name = this.name,
        points = listOf(this.points.toString())
    )
}*/