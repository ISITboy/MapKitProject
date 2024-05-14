package com.example.mapkitresultproject.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mapkitresultproject.domain.Constants
import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.domain.models.ShipperWithConsignee
import com.example.mapkitresultproject.domain.models.Transport
import kotlinx.coroutines.flow.Flow

@Dao
interface TransportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transport: Transport)

    @Update
    suspend fun update(transport: Transport)

    @Delete
    suspend fun delete(transport: Transport)

    @Query("SELECT * FROM ${Constants.TABLE_TRANSPORT}")
    fun getAllTransport(): Flow<List<Transport>>
}