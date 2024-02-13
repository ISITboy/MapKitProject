package com.example.mapkitresultproject.data.remote

import com.example.mapkitresultproject.data.remote.dto.distances.DistanceResponse
import com.example.mapkitresultproject.data.remote.dto.distances.Query
import com.example.mapkitresultproject.data.remote.dto.routes.RouteResponse
import com.example.mapkitresultproject.data.remote.utils.Constants.OPEN_ROUTE_SERVICE_API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface  RoutesApi {
    @Headers("Authorization: $OPEN_ROUTE_SERVICE_API_KEY")
    @POST("matrix/driving-car")
    suspend fun calculateDistance(@Body body: Query): Response<DistanceResponse>

    @GET("/v2/directions/driving-car")
    suspend fun getRoute(
        @retrofit2.http.Query("api_key") key: String = OPEN_ROUTE_SERVICE_API_KEY,
        @retrofit2.http.Query("start", encoded = true) start: String,
        @retrofit2.http.Query("end", encoded = true) end: String
    ):Response<RouteResponse>
}