package com.example.mapkitresultproject.domain.usecase.realtimeusecase

import com.example.mapkitresultproject.data.remote.FirebaseService
import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.domain.models.User.Base
import com.example.mapkitresultproject.domain.repository.ShipperRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val firebaseService: FirebaseService
) {
    fun getState() = firebaseService.getState()
    suspend operator fun invoke(user: Base){
        firebaseService.createUser(user)
    }
}