package com.example.mapkitresultproject.presentation.authorization.signup

import androidx.lifecycle.viewModelScope
import com.example.mapkitresultproject.data.repository.FirebaseServiceImpl
import com.example.mapkitresultproject.domain.models.User
import com.example.mapkitresultproject.domain.models.User.Base
import com.example.mapkitresultproject.domain.state.AuthResult
import com.example.mapkitresultproject.domain.usecase.authusescases.SignUpUseCase
import com.example.mapkitresultproject.domain.usecase.realtimeusecase.CreateUserUseCase
import com.example.mapkitresultproject.presentation.authorization.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val createUserUseCase: CreateUserUseCase
) : BaseViewModel() {

    override val sendRequest: suspend (String, String) -> AuthResult =
        { email, password -> signUpUseCase(email, password) }

    fun getState() = createUserUseCase.getState()

    fun createUser(user: Base) = viewModelScope.launch {
        createUserUseCase(user)
    }
}