package com.example.mapkitresultproject.domain.usecase.consignee

import com.example.mapkitresultproject.domain.repository.ConsigneeRepository
import javax.inject.Inject

class GetAllConsigneesUseCase @Inject constructor(
    private val consigneeRepository: ConsigneeRepository
) {
    operator fun invoke() = consigneeRepository.getAllConsignee()
}