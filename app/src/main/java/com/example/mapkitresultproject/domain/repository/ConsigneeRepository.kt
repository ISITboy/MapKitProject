package com.example.mapkitresultproject.domain.repository

import com.example.mapkitresultproject.domain.models.Consignee
import kotlinx.coroutines.flow.Flow

interface ConsigneeRepository {
    suspend fun insert(consignee: Consignee)
    suspend fun delete(consignee: Consignee)
    fun getAllConsignee(): Flow<List<Consignee>>
    suspend fun getConsignee(id: Int): Consignee?
}