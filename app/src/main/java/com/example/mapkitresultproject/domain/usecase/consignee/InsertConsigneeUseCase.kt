package com.example.mapkitresultproject.domain.usecase.consignee

import com.example.mapkitresultproject.domain.models.Consignee
import com.example.mapkitresultproject.domain.repository.ConsigneeRepository
import javax.inject.Inject

class InsertConsigneeUseCase @Inject constructor(
    private val consigneeRepository: ConsigneeRepository
) {
    suspend operator fun invoke(consignee: Consignee) {
        consigneeRepository.insert(consignee)
    }
}