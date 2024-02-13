package com.example.mapkitresultproject.domain.repository

import com.example.mapkitresultproject.domain.state.SearchState
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Queue

interface MapKitSearchRepository {
    fun getSearchState(): MutableStateFlow<SearchState>

    //    fun getResultedPoint(): LiveData<Point>
    fun setVisibleRegion(region: VisibleRegion?)
    fun setSearchOption(searchOptions: SearchOptions)
    fun createSession(query: String)
    fun clearObjectCollection()
    fun createSession(query: Queue<String>)
    fun setMapObjectTapListener(mapObjectTapListener: MapObjectTapListener)
    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection)

    fun subscribeForSearch(): Flow<*>

}