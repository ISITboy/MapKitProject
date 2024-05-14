package com.example.mapkitresultproject.data.repository


import com.example.mapkitresultproject.data.local.room.ConsigneeDao
import com.example.mapkitresultproject.domain.models.Consignee
import com.example.mapkitresultproject.domain.repository.ConsigneeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsigneeRepositoryImpl @Inject constructor(
    private val consigneeDao: ConsigneeDao
) : ConsigneeRepository {
    override suspend fun insert(consignee: Consignee) {
        consigneeDao.insert(consignee)
    }

    override suspend fun delete(consignee: Consignee) {
        consigneeDao.delete(consignee)
    }

    override suspend fun update(consignee: Consignee) {
        consigneeDao.update(consignee)
    }

    override fun getAllConsignee(): Flow<List<Consignee>> = consigneeDao.getAllConsignee()

    override suspend fun getConsignee(id: Int): Consignee? = consigneeDao.getConsignee(id)

}