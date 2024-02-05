package com.example.mapkitresultproject.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.Utils.getCoordinates
import com.example.mapkitresultproject.domain.repository.MapKitInteractionMapRepository
import com.yandex.mapkit.Animation
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.ScreenRect
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polygon
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MapKitInteractionMapRepositoryImpl @Inject constructor(
    private val context : Context
):MapKitInteractionMapRepository {

    private var routePoints = emptyList<Point>()
        set(value) {
            field = value
            onRoutePointsUpdated()
        }
    private val selectedPoint = MutableLiveData<Point>()
    private var placemarksCollection : MapObjectCollection?=null


    private val inputListener =  object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            Log.d("MyLog", "Tap on Map: ${getCoordinates(point)}")
        }

        override fun onMapLongTap(map: Map, point: Point) {
            Log.d("MyLog", "onMapLongTap: ${getCoordinates(point)}")
            selectedPoint.value = point
            routePoints = routePoints + point
            focusCamera(map)
        }
    }

    override fun addMapInputListener(map: Map) {
        map.addInputListener(inputListener)
    }

    override fun getSelectedPoint() = selectedPoint

    override fun setMapObjectCollection(mapObjectCollection: MapObjectCollection){
        placemarksCollection = mapObjectCollection
    }

    private fun onRoutePointsUpdated() {
        placemarksCollection?.clear()

        val imageProvider = ImageProvider.fromResource(context, R.drawable.bullet)
        routePoints.forEach {
            placemarksCollection?.addPlacemark(
                it,
                imageProvider,
                IconStyle().apply {
                    scale = 0.5f
                    zIndex = 20f
                }
            )
        }
    }



    private fun focusCamera(map: Map) {
        if (routePoints.isEmpty()) return

        val position = if (routePoints.size == 1) {
            map.cameraPosition.run {
                CameraPosition(routePoints.first(), zoom, azimuth, tilt)
            }
        } else {
            map.cameraPosition(Geometry.fromPolyline(Polyline(routePoints)))
        }

        map.move(position, Animation(Animation.Type.SMOOTH, 0.5f), null)

    }

}