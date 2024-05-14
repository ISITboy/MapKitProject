package com.example.mapkitresultproject.domain.repository

import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.domain.models.Transport
import kotlinx.coroutines.flow.Flow

interface TransportRepository {
    suspend fun insertTransport(transport:Transport)
    suspend fun updateTransport(transport:Transport)
    suspend fun deleteTransport(transport:Transport)
    fun getAllTransports(): Flow<List<Transport>>
}