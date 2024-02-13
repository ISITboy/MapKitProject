package com.example.mapkitresultproject.domain.repository

import com.example.mapkitresultproject.domain.state.SearchRouteState
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import kotlinx.coroutines.flow.MutableStateFlow

interface MapKitCreateRoutRepository {
    fun setDrivingOptions(drivingOptions : DrivingOptions)

    fun setRouteTapListener(mapObjectTapListener: MapObjectTapListener)
    fun setVehicleOptions(vehicleOptions: VehicleOptions)
    fun createSessionCreateRoute()
    fun clearRoutes()
    fun getCreateRouteState(): MutableStateFlow<SearchRouteState>
    fun setMapObjectRoutesCollection(mapObjectCollection: MapObjectCollection)
    fun onRoutesUpdated(routes: List<DrivingRoute>)
    fun getPolylinesMapsObjects(): MutableList<PolylineMapObject?>

    fun setPointForRoute(point: Point)
}