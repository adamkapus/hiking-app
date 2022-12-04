package com.adamkapus.hikingapp.domain.interactor.route

import com.adamkapus.hikingapp.data.disk.route.RouteDataSource
import com.adamkapus.hikingapp.domain.model.InteractorResponse
import com.adamkapus.hikingapp.domain.model.map.Route
import com.adamkapus.hikingapp.domain.model.toInteractorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RouteInteractor @Inject constructor(
    private val routeDataSource: RouteDataSource
) {

    suspend fun getRoutes(): InteractorResponse<List<Route>> = withContext(Dispatchers.IO) {
        routeDataSource.getRoutes().toInteractorResponse()
    }

    suspend fun getRoute(id: Int): InteractorResponse<Route> = withContext(Dispatchers.IO) {
        routeDataSource.getRoute(id).toInteractorResponse()
    }

}