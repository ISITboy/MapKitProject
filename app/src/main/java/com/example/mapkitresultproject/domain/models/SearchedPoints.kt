package com.example.mapkitresultproject.domain.models

import com.yandex.mapkit.geometry.Point

data class SearchedPoints(
    val names : MutableList<String> = mutableListOf(),
    val state: SearchState = SearchState.Off,
    val resultedPoints:MutableList<Point> =mutableListOf()
)