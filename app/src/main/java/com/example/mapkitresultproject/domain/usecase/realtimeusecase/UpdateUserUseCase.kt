package com.example.mapkitresultproject.domain.usecase.realtimeusecase

import com.example.mapkitresultproject.data.remote.FirebaseService
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val firebaseService: FirebaseService
) {
    fun getState() = firebaseService.getState()
    suspend operator fun invoke(name:String,organization:String){
        firebaseService.updateUser(name,organization)
    }
}