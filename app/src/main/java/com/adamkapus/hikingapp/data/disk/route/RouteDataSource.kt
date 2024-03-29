package com.adamkapus.hikingapp.data.disk.route

import com.adamkapus.hikingapp.data.model.CoordinateItem
import com.adamkapus.hikingapp.data.model.DataSourceResponse
import com.adamkapus.hikingapp.data.model.DataSourceResult
import com.adamkapus.hikingapp.data.model.RouteItem
import com.adamkapus.hikingapp.domain.model.map.Coordinate
import com.adamkapus.hikingapp.domain.model.map.Route
import javax.inject.Inject

class RouteDataSource @Inject constructor(
    private val routeDao: RouteDao,
    private val coordinateDao: CoordinateDao
) {

    suspend fun insertRoute(route: Route) {
        val insertedId = routeDao.insertRouteItem(RouteItem(id = null, name = route.name))
        for (coordinate in route.points) {
            coordinateDao.insertCoordinateItem(
                CoordinateItem(
                    id = null,
                    route_id = insertedId,
                    lat = coordinate.Lat,
                    lng = coordinate.Lng
                )
            )
        }

    }

    suspend fun getRoutes(): DataSourceResponse<List<Route>> {
        val routeItems = routeDao.getAllRouteItem()
        val result = mutableListOf<Route>()
        for (routeItem in routeItems) {
            val coordinateItems = coordinateDao.getRoutesCoordinateItem(routeItem.id!!)
            result.add(
                Route(
                    id = routeItem.id,
                    name = routeItem.name,
                    points = coordinateItems.map { Coordinate(Lat = it.lat, Lng = it.lng) }
                )
            )
        }
        return DataSourceResult(result)
    }

    suspend fun getRoute(id: Int): DataSourceResponse<Route> {
        val routeItem = routeDao.getRouteItem(id)
        val coordinateItems = coordinateDao.getRoutesCoordinateItem(routeItem.id!!)
        val list = coordinateItems.map { Coordinate(Lat = it.lat, Lng = it.lng) }
        return DataSourceResult(Route(id = routeItem.id, name = routeItem.name, points = list))
    }
}