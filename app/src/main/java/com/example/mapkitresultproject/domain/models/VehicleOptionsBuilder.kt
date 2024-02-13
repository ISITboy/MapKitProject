package com.example.mapkitresultproject.domain.models

import com.yandex.mapkit.directions.driving.VehicleType

data class VehicleOptions(
    val vehicleType: VehicleType,
    val maxWeight: Float?,
    val height: Float?,
    val width: Float?,
    val length: Float?
)

class VehicleOptionsBuilder() {
    var vehicleType = VehicleType.TRUCK
    var maxWeight: Float? = null
    var height: Float? = null
    var width: Float? = null
    var length: Float? = null

    fun setVehicleType(value: VehicleType): VehicleOptionsBuilder {
        this.vehicleType = value
        return this
    }

    fun setMaxWeight(value: Float?): VehicleOptionsBuilder {
        this.maxWeight = value
        return this
    }

    fun setHeight(value: Float?): VehicleOptionsBuilder {
        this.height = value
        return this
    }

    fun setWidth(value: Float?): VehicleOptionsBuilder {
        this.width = value
        return this
    }

    fun setLength(value: Float?): VehicleOptionsBuilder {
        this.length = value
        return this
    }

    fun build() = VehicleOptions(
        vehicleType = vehicleType,
        maxWeight = maxWeight,
        height = height,
        width = width,
        length = length
    )

}
