package com.example.mapkitresultproject.domain.models

import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition


class CameraPositionBuilder {
    private var location: Point = Point(53.677839, 23.829529) // г Гродн
    private var zoomValue: Float = 10.0f//Уровень масштабирования.
    private var azimuthValue :Float  =0.0f//Угол между севером и интересующим направлением на плоскости карты, в градусах в диапазоне [0, 360).
    private var tiltValue :Float = 0f//Наклон камеры в градусах.

    fun setLocation(location:Point) :CameraPositionBuilder{
        this.location = location
        return this
    }

    fun setZoom(zoom:Float) :CameraPositionBuilder{
        this.zoomValue = zoom
        return this
    }

    fun setAzimuth(azimuth:Float) :CameraPositionBuilder{
        this.azimuthValue = azimuth
        return this
    }

    fun setTilt(tilt:Float) :CameraPositionBuilder{
        this.tiltValue = tilt
        return this
    }

    fun build() = CameraPosition(
        location,
        zoomValue,
        azimuthValue,
        tiltValue
    )

}
