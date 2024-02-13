package com.example.mapkitresultproject.domain.usecase

import com.example.mapkitresultproject.domain.usecase.consignee.DeleteConsigneeUseCase
import com.example.mapkitresultproject.domain.usecase.consignee.GetAllConsigneesUseCase
import com.example.mapkitresultproject.domain.usecase.consignee.GetConsigneeUseCase
import com.example.mapkitresultproject.domain.usecase.consignee.InsertConsigneeUseCase
import javax.inject.Inject

data class ConsigneeUsesCases @Inject constructor(
    val deleteConsigneeUseCase: DeleteConsigneeUseCase,
    val insertConsigneeUseCase: InsertConsigneeUseCase,
    val getAllConsigneeUseCase: GetAllConsigneesUseCase,
    val getConsigneeUseCase: GetConsigneeUseCase
)