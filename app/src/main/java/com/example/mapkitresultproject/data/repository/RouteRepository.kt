package com.example.mapkitresultproject.data.repository

import com.example.mapkitresultproject.data.remote.RemoteDataSource
import com.example.mapkitresultproject.data.remote.dto.distances.DistanceResponse
import com.example.mapkitresultproject.data.remote.dto.distances.Query
import com.example.mapkitresultproject.data.remote.dto.routes.RouteResponse
import com.example.mapkitresultproject.data.remote.utils.BaseApiResponse
import com.example.mapkitresultproject.data.remote.utils.NetworkResult
import javax.inject.Inject

class RouteRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): BaseApiResponse(){
    suspend fun calculateDistance(body: Query): NetworkResult<DistanceResponse> {
        return safeApiCall { remoteDataSource.calculateDistance(body = body)}
    }

    suspend fun getRoute(start:String, end:String):NetworkResult<RouteResponse>{
        return safeApiCall { remoteDataSource.getRoute(start = start, end = end)}
    }

}