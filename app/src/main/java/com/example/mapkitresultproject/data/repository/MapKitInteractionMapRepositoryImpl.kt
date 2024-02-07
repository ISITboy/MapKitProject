package com.example.mapkitresultproject.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.Utils.getCoordinates
import com.example.mapkitresultproject.domain.repository.MapKitInteractionMapRepository
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.runtime.image.ImageProvider
import javax.inject.Inject

class MapKitInteractionMapRepositoryImpl @Inject constructor(
    private val context : Context
):MapKitInteractionMapRepository {

    private var selectedPointsForCreateRoute = emptyList<Point>()
        set(value) {
            field = value
            onRoutePointsUpdated()
        }
    private var currentSelectedPoint = MutableLiveData<Point>()

    private var placemarksCollection : MapObjectCollection?=null



    private val inputListener =  object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            Log.d("MyLog", "Tap on Map: ${getCoordinates(point)}")
        }

        override fun onMapLongTap(map: Map, point: Point) {
            Log.d("MyLog", "onMapLongTap: ${getCoordinates(point)}")
            currentSelectedPoint.value = point
            selectedPointsForCreateRoute = selectedPointsForCreateRoute + point
            focusCamera(map)
        }
    }
    override fun addMapInputListener(map: Map) {
        map.addInputListener(inputListener)
    }

    override fun getSelectedPoint() = currentSelectedPoint

    override fun clearSelectedPointsForCreateRoute(){
        placemarksCollection?.clear()
        selectedPointsForCreateRoute = listOf()
    }

    override fun setMapObjectPlacemarksCollection(mapObjectCollection: MapObjectCollection){
        placemarksCollection = mapObjectCollection.addCollection()
    }

    private fun onRoutePointsUpdated() {
        val imageProvider = ImageProvider.fromResource(context, R.drawable.bullet)
        selectedPointsForCreateRoute.forEach {
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
        if (selectedPointsForCreateRoute.isEmpty()) return

        val position = if (selectedPointsForCreateRoute.size == 1) {
            map.cameraPosition.run {
                CameraPosition(selectedPointsForCreateRoute.first(), zoom, azimuth, tilt)
            }
        } else {
            map.cameraPosition(Geometry.fromPolyline(Polyline(selectedPointsForCreateRoute)))
        }

        map.move(position, Animation(Animation.Type.SMOOTH, 0.5f), null)

    }

}