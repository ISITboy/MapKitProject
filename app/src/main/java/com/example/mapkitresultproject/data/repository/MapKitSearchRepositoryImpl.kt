package com.example.mapkitresultproject.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.domain.models.SearchResponseItem
import com.example.mapkitresultproject.domain.models.SearchState
import com.example.mapkitresultproject.domain.models.SelectedObjectHolder
import com.example.mapkitresultproject.domain.repository.MapKitSearchRepository
import com.example.mapkitresultproject.presentation.detailsscreen.DetailsFragment
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MapKitSearchRepositoryImpl @Inject constructor(
    private val context: Context
) : MapKitSearchRepository {

    private lateinit var searchManager: SearchManager
    private lateinit var searchOptions: SearchOptions
    private var searchSession: Session? = null
    private var resultResponse = MutableLiveData<Point>()
    private val region = MutableStateFlow<VisibleRegion?>(null)
    private val searchState = MutableStateFlow<SearchState>(SearchState.Off)
    private var objectCollection: MapObjectCollection? = null
    private lateinit var searchResultPlacemarkTapListener: MapObjectTapListener

    override fun setMapObjectTapListener(mapObjectTapListener:MapObjectTapListener){
        searchResultPlacemarkTapListener = mapObjectTapListener
    }

    override fun getSearchState() = searchState.value

    override fun getResultedPoint() = resultResponse

    override fun setVisibleRegion(region: VisibleRegion) {
        this.region.value = region
    }

    override fun setSearchManager(searchManager: SearchManager) {
        this.searchManager = searchManager
    }

    override fun setSearchOption(resultPageSize: Int, searchTypes: SearchType) {
        searchOptions = SearchOptions().apply {
            this.resultPageSize = resultPageSize
            this.searchTypes =  searchTypes.value
        }
    }

    override fun createSession(
        query: String,
        geometry: Geometry
    ) {
        objectCollection?.clear()
        searchSession?.cancel()
        searchSession = searchManager.submit(
            query,
            geometry,
            searchOptions,
            searchListener
        )
        searchState.value = SearchState.Loading
    }

    private val searchListener = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            val items = response.collection.children.mapNotNull {
                val point = it.obj?.geometry?.firstOrNull()?.point ?: return@mapNotNull null
                SearchResponseItem(point, it.obj)
            }
            val boundingBox = response.metadata.boundingBox ?: BoundingBox()
            updateSearchResponsePlacemarks(items)
            searchState.value = SearchState.Off
        }

        override fun onSearchError(error: Error) {
            searchState.value = SearchState.Error()
            when (error) {
                is NetworkError -> (searchState.value as SearchState.Error).message =
                    "Search request error due network issues"

                else -> (searchState.value as SearchState.Error).message =
                    "Search request unknown error"
            }
        }

    }

    override fun setMapObjectCollection(mapObjectCollection: MapObjectCollection) {
        objectCollection = mapObjectCollection
    }

    private fun updateSearchResponsePlacemarks(items: List<SearchResponseItem>) {

        val imageProvider = ImageProvider.fromResource(context, R.drawable.search_result)

        items.forEach {
            objectCollection?.addPlacemark(
                it.point,
                imageProvider,
                IconStyle().apply { scale = 0.5f }
            )?.apply {
                addTapListener(searchResultPlacemarkTapListener)
                userData = it.geoObject
            }
        }

    }



}