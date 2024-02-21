package com.example.mapkitresultproject.domain.state

import com.example.mapkitresultproject.domain.models.User

sealed class AuthResult {

    class Success(val user: User) : AuthResult()

    class Error(val e: Exception) : AuthResult()

    object Loading : AuthResult()
}