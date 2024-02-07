package com.example.mapkitresultproject.domain.usecase

import com.example.mapkitresultproject.domain.repository.MapKitSearchRepository
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchType
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: MapKitSearchRepository
) {

    fun setVisibleRegion(region: VisibleRegion?) {
        searchRepository.setVisibleRegion(region = region)
    }
    fun setSearchManager(searchManager: SearchManager) {
        searchRepository.setSearchManager(searchManager = searchManager)
    }
    fun getSearchState() = searchRepository.getSearchState()

//    fun getResultedPoint() = searchRepository.getResultedPoint()

    fun setSearchOption(resultPageSize: Int, searchTypes: SearchType) {
        searchRepository.setSearchOption(
            resultPageSize = resultPageSize,
            searchTypes = searchTypes
        )
    }

    fun createSession(query: String) {
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