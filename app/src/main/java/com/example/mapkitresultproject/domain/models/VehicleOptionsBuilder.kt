package com.example.mapkitresultproject.domain.models

import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.directions.driving.VehicleType

class VehicleOptionsBuilder() {
    private var vehicleType = VehicleType.TRUCK
    private var weight: Float? = null
    private var axleWeight: Float? = null
    private var maxWeight: Float? = null
    private var height: Float? = null
    private var width: Float? = null
    private var length: Float? = null
    private var payload: Float? = null
    private var ecoClass: Int? = null
    private var hasTrailer: Boolean? = null
    private var buswayPermitted: Boolean? = null

    fun setVehicleType(value: VehicleType): VehicleOptionsBuilder {
        this.vehicleType = value
        return this
    }

    fun setMaxWeight(value: Float?): VehicleOptionsBuilder {
        this.maxWeight = value
        return this
    }

    fun setAxleWeight(value: Float?): VehicleOptionsBuilder {
        this.axleWeight = value
        return this
    }

    fun setPayload(value: Float?): VehicleOptionsBuilder {
        this.payload = value
        return this
    }

    fun setEcoClass(value: Int?): VehicleOptionsBuilder {
        this.ecoClass = value
        return this
    }

    fun setHasTrailer(value: Boolean?): VehicleOptionsBuilder {
        this.hasTrailer = value
        return this
    }

    fun setBuswayPermitted(value: Boolean?): VehicleOptionsBuilder {
        this.buswayPermitted = value
        return this
    }

    fun setWeight(value: Float?): VehicleOptionsBuilder {
        this.weight = value
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
        vehicleType,
        weight,
        axleWeight,
        maxWeight,
        height,
        width,
        length,
        payload,
        ecoClass,
        hasTrailer,
        buswayPermitted
    )

}
