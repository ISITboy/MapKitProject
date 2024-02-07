package com.example.mapkitresultproject.domain.models

import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Polyline

interface SearchRouteState {
    data object Off : SearchRouteState
    data object Loading : SearchRouteState
    class Error() : SearchRouteState{
        private var _message:String=""
        var message:String
            get() = _message
            set(value) {
                _message = value
            }

    }
    data class Success(
        val drivingRoutes: MutableList<DrivingRoute>,
        val polyline:  List<Polyline>
    ) : SearchRouteState
}