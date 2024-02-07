package com.example.mapkitresultproject.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mapkitresultproject.domain.models.SearchState
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface MapKitSearchRepository {
    fun getSearchState(): MutableStateFlow<SearchState>

    //    fun getResultedPoint(): LiveData<Point>
    fun setVisibleRegion(region: VisibleRegion?)
    fun setSearchManager(searchManager: SearchManager)
    fun setSearchOption(resultPageSize: Int, searchTypes: SearchType)
    fun createSession(query: String)
    fun clearObjectCollection()
    fun setMapObjectTapListener(mapObjectTapListener: MapObjectTapListener)
    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection)

    fun subscribeForSearch(): Flow<*>

}