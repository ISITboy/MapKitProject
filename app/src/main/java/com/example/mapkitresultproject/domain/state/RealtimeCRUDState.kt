package com.example.mapkitresultproject.domain.state

import com.example.mapkitresultproject.domain.models.User
import com.yandex.mapkit.geometry.BoundingBox

sealed interface RealtimeCRUDState {

    data object Off : RealtimeCRUDState

    data class Error(val message:String) : RealtimeCRUDState

    data class Success(val message:String = "loading...",val user:User.Base = User.Base()) : RealtimeCRUDState
}