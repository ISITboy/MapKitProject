package com.example.mapkitresultproject.presentation.tabs.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapkitresultproject.domain.usecase.realtimeusecase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditProfileFragmentViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase
):ViewModel() {

    fun updateUser(name:String,organization:String) = viewModelScope.launch {
        updateUserUseCase(name,organization)
    }
}