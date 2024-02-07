package com.example.mapkitresultproject.domain.models

import com.yandex.mapkit.geometry.Point

data class StartLocation(
    val location: Point = Point(53.685274, 23.837227),
    val zoomValue: Float = 10.0f,//Уровень масштабирования.
    val azimuthValue: Float = 0.0f,//Угол между севером и интересующим направлением на плоскости карты, в градусах в диапазоне [0, 360).
    val tiltValue: Float = 0f//Наклон камеры в градусах.
)
