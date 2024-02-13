package com.example.mapkitresultproject.data.remote

import com.example.mapkitresultproject.data.remote.dto.distances.Query
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val routesApi: RoutesApi
) {
    suspend fun calculateDistance(body: Query) = routesApi.calculateDistance(body = body)

    suspend fun getRoute(start:String, end:String) = routesApi.getRoute(start = start, end = end)
}