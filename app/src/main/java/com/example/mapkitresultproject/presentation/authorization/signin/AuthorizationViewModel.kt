package com.example.mapkitresultproject.presentation.authorization.signin

import com.example.mapkitresultproject.domain.repository.AuthRepository
import com.example.mapkitresultproject.domain.state.AuthResult
import com.example.mapkitresultproject.domain.usecase.authusescases.SignInUseCase
import com.example.mapkitresultproject.domain.usecase.sharedpreferences.SaveUIDUsesCase
import com.example.mapkitresultproject.presentation.authorization.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val auth: FirebaseAuth,
    private val saveUIDUsesCase: SaveUIDUsesCase
) : BaseViewModel() {

    override val sendRequest: suspend (String, String) -> AuthResult =
        { email, password -> signInUseCase(email, password) }

    fun saveUID() {
        auth.uid?.let { saveUIDUsesCase.invoke(it) }
    }
}