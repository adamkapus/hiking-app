package com.adamkapus.hikingapp.domain.model.map

data class Route(
    val name: String?,
    val description: String?,
    val points: List<Coordinate>

)

data class Coordinate(
    val Lat: Double,
    val Lng: Double
)