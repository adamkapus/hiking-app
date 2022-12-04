package com.adamkapus.hikingapp.domain.model.map

data class Route(
    val id : Int?,
    val name: String,
    val points: List<Coordinate>

)

data class Coordinate(
    val Lat: Double,
    val Lng: Double
)