package com.example.mapkitresultproject.domain.models

import com.yandex.mapkit.annotations.AnnotationLanguage
import com.yandex.mapkit.directions.driving.DrivingOptions

class DrivingOptionsBuilder{

    private var routesCount:Int? = 1
    private var avoidTolls :Boolean? = null
    private var avoidUnpaved :Boolean? = null
    private var avoidPoorConditions :Boolean?= null
    private var initialAzimuth :Double? = null
    private var departureTime : Long? = null
    private var annotationLanguage :AnnotationLanguage? = null

    fun setRoutesCount(value: Int?):DrivingOptionsBuilder{
        this.routesCount = value
        return this
    }
    fun setAvoidTolls(value: Boolean?):DrivingOptionsBuilder{
        this.avoidTolls = value
        return this
    }

    fun setAvoidUnpaved(value: Boolean?):DrivingOptionsBuilder{
        this.avoidUnpaved = value
        return this
    }

    fun setAvoidPoorConditions(value: Boolean?):DrivingOptionsBuilder{
        this.avoidPoorConditions = value
        return this
    }

    fun build() = DrivingOptions(
        initialAzimuth,
        routesCount,
        avoidTolls,
        avoidUnpaved,
        avoidPoorConditions,
        departureTime,
        annotationLanguage
    )


}