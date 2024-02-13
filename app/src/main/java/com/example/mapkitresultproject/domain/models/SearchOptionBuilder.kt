package com.example.mapkitresultproject.domain.models

import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.FilterCollection
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType

class SearchOptionBuilder {

    private var searchTypes = SearchType.NONE.value
    private var geometry = false
    private var disableSpellingCorrection = false
    private var resultPageSize :Int? = 10
    private var userPosition : Point?= null
    private var origin : String? = null
    private var filters :FilterCollection?= null

    fun setSearchTypes(searchTypes:SearchType):SearchOptionBuilder{
        this.searchTypes = searchTypes.value
        return this
    }
    fun setGeometry(geometry:Boolean):SearchOptionBuilder{
        this.geometry = geometry
        return this
    }
    fun setDisableSpellingCorrection(disableSpellingCorrection:Boolean):SearchOptionBuilder{
        this.disableSpellingCorrection = disableSpellingCorrection
        return this
    }
    fun setResultPageSize(resultPageSize:Int?):SearchOptionBuilder{
        this.resultPageSize = resultPageSize
        return this
    }
    fun setUserPosition(userPosition:Point?):SearchOptionBuilder{
        this.userPosition = userPosition
        return this
    }
    fun setOrigin(origin:String?):SearchOptionBuilder{
        this.origin = origin
        return this
    }
    fun setFilters(filters:FilterCollection?):SearchOptionBuilder{
        this.filters = filters
        return this
    }

    fun build()= SearchOptions(
        searchTypes,
        resultPageSize,
        userPosition,
        origin,
        geometry,
        disableSpellingCorrection,
        filters
    )
}