package com.example.mapkitresultproject.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mapkitresultproject.domain.models.SearchState
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection

interface MapKitCreateRoutRepository {
    fun setDrivingRouter(drivingRouter: DrivingRouter)
    fun setDrivingOptions(
        routesCount: Int, avoidTolls: Boolean,
        avoidPoorConditions: Boolean,
        avoidUnpaved: Boolean
    )
    fun setVehicleOptions(vehicleType: VehicleType, weight:Float)
    fun createSessionCreateRoute()
    fun setPointsForRoute(points: List<Point>)
    fun clearPointsForRoute()
    fun getCreateRouteState(): SearchState
    fun getResultedRout(): LiveData<List<DrivingRoute>>
    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection)
    fun onRoutesUpdated(routes: List<DrivingRoute>)

    fun setPointForRoute(point: Point)
}