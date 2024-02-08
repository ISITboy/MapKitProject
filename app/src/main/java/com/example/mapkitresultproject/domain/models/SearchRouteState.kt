package com.example.mapkitresultproject.domain.models

import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Polyline

interface SearchRouteState {
    data object Off : SearchRouteState
    data object Loading : SearchRouteState
    data class Error(val message:String) : SearchRouteState
    data class Success(
        val drivingRoutes: MutableList<DrivingRoute>,
        val polyline:  List<Polyline>
    ) : SearchRouteState
}