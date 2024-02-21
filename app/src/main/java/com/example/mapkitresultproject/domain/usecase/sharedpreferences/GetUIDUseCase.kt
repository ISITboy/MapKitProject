package com.example.mapkitresultproject.domain.usecase.sharedpreferences

import com.example.mapkitresultproject.domain.repository.SharedPrefUserStorageRepository
import javax.inject.Inject

class GetUIDUseCase @Inject constructor(
    private val sharedPrefUserStorageRepository: SharedPrefUserStorageRepository
) {
    operator fun invoke() = sharedPrefUserStorageRepository.getUID()

}