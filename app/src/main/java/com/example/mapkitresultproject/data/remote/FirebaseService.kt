package com.example.mapkitresultproject.data.remote

import com.example.mapkitresultproject.domain.models.User.Base
import com.example.mapkitresultproject.domain.state.RealtimeCRUDState
import kotlinx.coroutines.flow.MutableStateFlow

interface FirebaseService {
    fun getState(): MutableStateFlow<RealtimeCRUDState>
    suspend fun createUser(user:Base)
    suspend fun updateUser(name:String,organization:String)

    suspend fun readUser()
}