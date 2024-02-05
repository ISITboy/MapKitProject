package com.example.mapkitresultproject

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.yandex.mapkit.geometry.Point

object Utils {
    fun getCoordinates(point: Point) = "${point.latitude} ; ${point.longitude}"

    fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }

    fun <T : View, V> T.goneOrRun(value: V?, block: T.(V) -> Unit) {
        this.isVisible = value != null
        if (value != null) {
            this.block(value)
        }
    }

    fun <T> List<T>.takeIfNotEmpty(): List<T>? = takeIf { it.isNotEmpty() }

}