package com.example.mapkitresultproject.domain.usecase

import com.example.mapkitresultproject.domain.usecase.sharedpreferences.DeleteUIDUseCase
import com.example.mapkitresultproject.domain.usecase.sharedpreferences.GetUIDUseCase
import com.example.mapkitresultproject.domain.usecase.sharedpreferences.SaveUIDUsesCase
import javax.inject.Inject

data class SharedPrefUserStorageUsesCases @Inject constructor(
    val deleteUIDUseCase: DeleteUIDUseCase,
    val getUIDUseCase: GetUIDUseCase,
    val saveUIDUsesCase: SaveUIDUsesCase
)