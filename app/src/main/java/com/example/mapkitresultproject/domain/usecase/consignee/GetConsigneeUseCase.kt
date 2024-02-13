package com.example.mapkitresultproject.domain.usecase.consignee

import com.example.mapkitresultproject.domain.repository.ConsigneeRepository
import javax.inject.Inject

class GetConsigneeUseCase @Inject constructor(
    private val consigneeRepository: ConsigneeRepository
) {
    suspend operator fun invoke(id:Int){
        consigneeRepository.getConsignee(id)
    }
}