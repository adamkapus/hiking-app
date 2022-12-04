package com.adamkapus.hikingapp.data.disk.route

import android.util.Log
import com.adamkapus.hikingapp.data.model.*
import com.adamkapus.hikingapp.domain.model.map.Coordinate
import com.adamkapus.hikingapp.domain.model.map.Route
import javax.inject.Inject

class RouteDataSource @Inject constructor(
    private val routeDao: RouteDao,
    private val coordinateDao: CoordinateDao
) {

    suspend fun insertRoute(route: Route) {
        val routeId = routeDao.insertRouteItem(RouteItem(id = null, name = route.name))
        for (coordinate in route.points) {
            coordinateDao.insertCoordinateItem(
                CoordinateItem(
                    id = null,
                    route_id = routeId,
                    lat = coordinate.Lat,
                    lng = coordinate.Lng
                )
            )
        }

    }

    suspend fun getRoutes(): DataSourceResponse<List<Route>> {
        val map = routeDao.getAllRouteItem()
        Log.d("PLS", "legyszi " + map.toString())
        val result = mutableListOf<Route>()
        for (route in map.entries) {
            result.add(Route(
                id = route.key.id,
                name = route.key.name,
                points = route.value.map { Coordinate(Lat = it.lat, Lng = it.lng) }
            ))
        }
        return DataSourceResult(result)//DataSourceResult(routeDao.getAllRouteItem().map { it.toRoute() })
    }

    suspend fun getRoute(id: Int): DataSourceResponse<Route> {
        val map = routeDao.getAllRouteItem()
        val list = mutableListOf<Route>()
        for (route in map.entries) {
            list.add(Route(
                id = route.key.id,
                name = route.key.name,
                points = route.value.map { Coordinate(Lat = it.lat, Lng = it.lng) }
            ))
        }
        val result = list.find { it.id == id }
        return if (result == null) {
            DataSourceError
        } else {
            DataSourceResult(result)
        }
        //DataSourceResult(routeDao.getAllRouteItem().map { it.toRoute() })
    }
}