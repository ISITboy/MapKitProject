package com.example.mapkitresultproject.domain.repository

import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.domain.models.ShipperWithConsignee
import kotlinx.coroutines.flow.Flow

interface ShipperRepository {

    suspend fun insertShipper(shipper: Shipper)
    suspend fun updateShipper(shipper: Shipper)
    suspend fun deleteShipper(shipper: Shipper)
    fun getAllShippers(): Flow<List<Shipper>>
    suspend fun getShipper(id: Int): Shipper?
    suspend fun getShipperWithConsignee():List<ShipperWithConsignee>
}