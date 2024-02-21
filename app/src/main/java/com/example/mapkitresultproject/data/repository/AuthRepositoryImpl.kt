package com.example.mapkitresultproject.data.repository

import android.util.Log
import com.example.mapkitresultproject.domain.models.User
import com.example.mapkitresultproject.domain.repository.AuthRepository
import com.example.mapkitresultproject.domain.state.AuthResult
import com.example.mapkitresultproject.domain.usecase.SharedPrefUserStorageUsesCases
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val sharedPrefUserStorageUsesCases: SharedPrefUserStorageUsesCases
) : AuthRepository {
    override suspend fun signOut(){
        auth.signOut()
        sharedPrefUserStorageUsesCases.deleteUIDUseCase.invoke()
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): AuthResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user!!
            sharedPrefUserStorageUsesCases.saveUIDUsesCase.invoke(user.uid)
            AuthResult.Success(User.Base(user.email ?: " ", user.uid))
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }

    override suspend fun signUpWithEmailAndPassword(email: String, password: String): AuthResult {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user!!
            AuthResult.Success(User.Base(user.email ?: " ", user.uid))
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }
}