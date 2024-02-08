package com.example.mapkitresultproject.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mapkitresultproject.domain.models.SearchRouteState
import com.example.mapkitresultproject.domain.models.SearchState
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import kotlinx.coroutines.flow.MutableStateFlow

interface MapKitCreateRoutRepository {
    fun setDrivingRouter(drivingRouter: DrivingRouter)
    fun setDrivingOptions(
        routesCount: Int, avoidTolls: Boolean,
        avoidPoorConditions: Boolean,
        avoidUnpaved: Boolean
    )

    fun setRouteTapListener(mapObjectTapListener: MapObjectTapListener)
    fun setVehicleOptions(vehicleType: VehicleType, weight:Float)
    fun createSessionCreateRoute()
    fun clearRoutes()
    fun getCreateRouteState(): MutableStateFlow<SearchRouteState>
    fun setMapObjectRoutesCollection(mapObjectCollection: MapObjectCollection)
    fun onRoutesUpdated(map:Map,routes: List<DrivingRoute>)
    fun getPolylinesMapsObjects(): MutableList<PolylineMapObject?>

    fun setPointForRoute(point: Point)
}