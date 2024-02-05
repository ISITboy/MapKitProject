package com.example.mapkitresultproject.domain.repository

import androidx.lifecycle.MutableLiveData
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection

interface MapKitInteractionMapRepository {
    fun addMapInputListener(map :Map)
    fun getSelectedPoint(): MutableLiveData<Point>
    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection)
}