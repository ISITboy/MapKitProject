package com.example.mapkitresultproject.domain.models

data class DrivingOptions (
    val routesCount : Int?,
    val avoidTolls : Boolean?,
    val avoidUnpaved :Boolean?,
    val avoidPoorConditions:Boolean?
)

class DrivingOptionsBuilder{

    var routesCount:Int? = 1
    var avoidTolls :Boolean? = null
    var avoidUnpaved :Boolean? = null
    var avoidPoorConditions :Boolean?= null

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
        routesCount = routesCount,
        avoidTolls = avoidTolls,
        avoidUnpaved = avoidUnpaved,
        avoidPoorConditions= avoidPoorConditions
    )


}