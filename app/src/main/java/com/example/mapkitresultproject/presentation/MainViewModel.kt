package com.example.mapkitresultproject.presentation

import androidx.lifecycle.ViewModel
import com.example.mapkitresultproject.domain.usecase.sharedpreferences.GetUIDUseCase
import com.example.mapkitresultproject.domain.usecase.sharedpreferences.SaveUIDUsesCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUIDUseCase: GetUIDUseCase
):ViewModel() {
    fun checkUID():String = getUIDUseCase()
}