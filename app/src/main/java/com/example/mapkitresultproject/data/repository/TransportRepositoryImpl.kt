package com.example.mapkitresultproject.data.repository

import com.example.mapkitresultproject.data.local.room.TransportDao
import com.example.mapkitresultproject.domain.models.Transport
import com.example.mapkitresultproject.domain.repository.TransportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransportRepositoryImpl @Inject constructor(
    val transportDao: TransportDao
) : TransportRepository {
    override suspend fun insertTransport(transport: Transport) {
        transportDao.insert(transport)
    }

    override suspend fun updateTransport(transport: Transport) {
        transportDao.update(transport)
    }

    override suspend fun deleteTransport(transport: Transport) {
        transportDao.delete(transport)
    }

    override fun getAllTransports(): Flow<List<Transport>> = transportDao.getAllTransport()
}