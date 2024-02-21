package com.example.mapkitresultproject.presentation.tabs.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapkitresultproject.domain.models.User
import com.example.mapkitresultproject.domain.repository.AuthRepository
import com.example.mapkitresultproject.domain.usecase.authusescases.SignOutUseCase
import com.example.mapkitresultproject.domain.usecase.realtimeusecase.ReadUserUseCase
import com.example.mapkitresultproject.domain.usecase.sharedpreferences.DeleteUIDUseCase
import com.example.mapkitresultproject.domain.usecase.sharedpreferences.GetUIDUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val readUserUseCase: ReadUserUseCase
) :ViewModel(){
    fun getState() = readUserUseCase.getState()
    fun signOut() = viewModelScope.launch{
        signOutUseCase()
    }

    fun readUser()=viewModelScope.launch {
        readUserUseCase()
    }
}