package com.example.mapkitresultproject.domain.models

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.map.PolylineMapObject

object SelectedObjectHolder {
    var selectedObject: GeoObject? = null
}

object SelectedRouteHolder {
    var selectedRoute:  MutableList<PolylineMapObject?> = mutableListOf()
}
