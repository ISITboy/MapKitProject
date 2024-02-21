package com.example.mapkitresultproject.domain.repository

import com.example.mapkitresultproject.domain.state.AuthResult


interface AuthRepository {

    suspend fun signOut()

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult

    suspend fun signUpWithEmailAndPassword(email: String, password: String): AuthResult
}