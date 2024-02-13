package com.example.mapkitresultproject.domain.usecase.mapkitusescases

import com.example.mapkitresultproject.domain.repository.MapKitSearchRepository
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import java.util.Queue
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: MapKitSearchRepository
) {

    fun setVisibleRegion(region: VisibleRegion?) {
        searchRepository.setVisibleRegion(region = region)
    }
    fun getSearchState() = searchRepository.getSearchState()

//    fun getResultedPoint() = searchRepository.getResultedPoint()

    fun setSearchOption(searchOptions: SearchOptions) {
        searchRepository.setSearchOption(searchOptions = searchOptions)
    }

    fun createSession(query: String) {
        searchRepository.createSession(query = query)
    }

    fun createSession(query: Queue<String>) {
        searchRepository.createSession(query = query)
    }

    fun clearObjectCollection(){
        searchRepository.clearObjectCollection()
    }

    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection) = searchRepository.setMapObjectCollection(
        mapObjectCollection = mapObjectCollection
    )

    fun setMapObjectTapListener(mapObjectTapListener: MapObjectTapListener){
        searchRepository.setMapObjectTapListener(mapObjectTapListener = mapObjectTapListener)
    }

    fun subscribeForSearch() = searchRepository.subscribeForSearch()


}