package com.example.mapkitresultproject.domain.usecase.authusescases

import com.example.mapkitresultproject.data.remote.dto.distances.Query
import com.example.mapkitresultproject.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.signInWithEmailAndPassword(email, password)
}