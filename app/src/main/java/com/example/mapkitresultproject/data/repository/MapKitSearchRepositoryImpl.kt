package com.example.mapkitresultproject.data.repository

import android.content.Context
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.domain.models.SearchOptionBuilder
import com.example.mapkitresultproject.domain.state.SearchResponseItem
import com.example.mapkitresultproject.domain.state.SearchState
import com.example.mapkitresultproject.domain.repository.MapKitSearchRepository
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Queue
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class MapKitSearchRepositoryImpl @Inject constructor(
    private val context: Context
) : MapKitSearchRepository {

    private val searchManager = SearchFactory.getInstance()
        .createSearchManager(SearchManagerType.COMBINED)
    private var searchOptions = SearchOptionBuilder().build()
    private var searchSession: Session? = null

    private var objectCollection: MapObjectCollection? = null
    private lateinit var searchResultPlacemarkTapListener: MapObjectTapListener

    private val region = MutableStateFlow<VisibleRegion?>(null)

    @OptIn(FlowPreview::class)
    private val throttledRegion = region.debounce(1.seconds)

    private val searchState = MutableStateFlow<SearchState>(SearchState.Off)

    private var zoomToSearchResult = false

    override fun setMapObjectTapListener(mapObjectTapListener: MapObjectTapListener) {
        searchResultPlacemarkTapListener = mapObjectTapListener
    }

    override fun getSearchState() = searchState


    override fun setVisibleRegion(region: VisibleRegion?) {
        this.region.value = region
    }
    override fun setSearchOption(searchOptions: SearchOptions) {
        this.searchOptions = searchOptions
    }

    override fun createSession(query: String) {
        val region = region.value?.let {
            VisibleRegionUtils.toPolygon(it)
        } ?: return
        searchSession?.cancel()
        searchSession = searchManager.submit(
            query,
            region,
            searchOptions,
            searchListener
        )
        searchState.value = SearchState.Loading
        zoomToSearchResult = true
    }

    override fun createSession(query: Queue<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            while (query.isNotEmpty()) {
                if (searchState.value !is SearchState.Loading) {
                    query.poll()?.let {
                        runBlocking(Dispatchers.Main) {
                            searchManager.submit(
                                it,
                                Geometry.fromBoundingBox(
                                    BoundingBox(
                                        Point(0.0, 0.0),
                                        Point(90.0, 180.0)
                                    )
                                ),
                                searchOptions,
                                searchListener
                            )
                            searchState.value = SearchState.Loading
                        }
                    }
                }
            }
        }
    }


    private val searchListener = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            val items = response.collection.children.mapNotNull {
                val point = it.obj?.geometry?.firstOrNull()?.point ?: return@mapNotNull null
                SearchResponseItem(point, it.obj)
            }
            val boundingBox =
                response.metadata.boundingBox ?: BoundingBox(Point(0.0, 0.0), Point(90.0, 180.0))
            updateSearchResponsePlacemarks(items)
            searchState.value = SearchState.Success(
                items = items,
                zoomToSearchResult,
                itemsBoundingBox = boundingBox
            )
        }

        override fun onSearchError(error: Error) {
            when (error) {
                is NetworkError -> searchState.value =
                    SearchState.Error("Search request error due network issues")

                else -> searchState.value = SearchState.Error("Search request unknown error")
            }
        }

    }

    override fun setMapObjectCollection(mapObjectCollection: MapObjectCollection) {
        objectCollection = mapObjectCollection.addCollection()
    }

    private fun updateSearchResponsePlacemarks(items: List<SearchResponseItem>) {
        clearObjectCollection()
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

    override fun clearObjectCollection() {
        objectCollection?.clear()
    }

    override fun subscribeForSearch(): Flow<*> {
        return throttledRegion.filter { it != null }
            .filter { searchState.value is SearchState.Success }
            .mapNotNull { it }
            .onEach { region ->
                searchSession?.let {
                    it.setSearchArea(VisibleRegionUtils.toPolygon(region))
                    it.resubmit(searchListener)
                    searchState.value = SearchState.Loading
                    zoomToSearchResult = false
                }
            }
    }

}