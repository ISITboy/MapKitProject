package com.example.mapkitresultproject.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.mapkitresultproject.domain.models.SearchState
import com.example.mapkitresultproject.domain.repository.MapKitSearchRepository
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MapKitSearchRepositoryImpl @Inject constructor():MapKitSearchRepository {

    private lateinit var searchManager: SearchManager
    private lateinit var searchOptions: SearchOptions
    private var searchSession: Session? = null
    private var resultResponse = MutableLiveData<Point>()
    private val region = MutableStateFlow<VisibleRegion?>(null)
    private val searchState = MutableStateFlow<SearchState>(SearchState.Off)

    override fun getSearchState() = searchState.value

    override fun getResultedPoint()= resultResponse

    override fun setVisibleRegion(region: VisibleRegion) {
        this.region.value = region
    }

    override fun setSearchManager(searchManager: SearchManager) {
        this.searchManager = searchManager
    }

    override fun setSearchOption(resultPageSize: Int, searchTypes: SearchType) {
        searchOptions = SearchOptions().apply {
            this.resultPageSize = resultPageSize
            this.searchTypes = searchTypes.value
        }
    }

    override fun createSession(
        query: String,
        geometry: Geometry
    ) {
            searchSession?.cancel()
            searchSession = searchManager.submit(
                query,
                geometry,
                searchOptions,
                searchListener
            )
        searchState.value = SearchState.Loading
    }

    private val searchListener = object :Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            val items = response.collection.children.mapNotNull {
                val point = it.obj?.geometry?.firstOrNull()?.point
                resultResponse.value=point!!
                searchState.value = SearchState.Off
            }
        }

        override fun onSearchError(error: Error) {
            searchState.value = SearchState.Error(message = "onSearchError")
        }

    }
}