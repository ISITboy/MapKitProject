package com.example.mapkitresultproject.domain.usecase.sharedpreferences

import com.example.mapkitresultproject.domain.repository.SharedPrefUserStorageRepository
import javax.inject.Inject

class SaveUIDUsesCase @Inject constructor(
    private val sharedPrefUserStorageRepository: SharedPrefUserStorageRepository
) {
    operator fun invoke(uid:String) = sharedPrefUserStorageRepository.saveUID(uid)

}