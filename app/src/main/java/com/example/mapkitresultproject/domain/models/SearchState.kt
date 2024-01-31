package com.example.mapkitresultproject.domain.models

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point

sealed interface SearchState {
    data object Off : SearchState
    data object Loading : SearchState
    class Error() : SearchState{
        private var _message:String=""
        var message:String
            get() = _message
            set(value) {
                _message = value
            }

    }
    data class Success(
        val items: List<SearchResponseItem>,
        val zoomToItems: Boolean,
        val itemsBoundingBox: BoundingBox,
    ) : SearchState
}

data class SearchResponseItem(
    val point: Point,
    val geoObject: GeoObject?,
)