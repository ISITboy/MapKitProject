package com.example.mapkitresultproject.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.mapkitresultproject.domain.Constants.TABLE_SHIPPER
import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.domain.models.ShipperWithConsignee
import kotlinx.coroutines.flow.Flow

@Dao
interface ShipperDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shipper: Shipper)
    @Update
    suspend fun update(shipper: Shipper)

    @Delete
    suspend fun delete(shipper: Shipper)

    @Query("SELECT * FROM $TABLE_SHIPPER")
    fun getAllShippers(): Flow<List<Shipper>>

    @Query("SELECT * FROM $TABLE_SHIPPER WHERE id=:id")
    suspend fun getShipper(id: Int): Shipper?

    @Transaction
    @Query("SELECT * FROM $TABLE_SHIPPER")
    suspend fun getShipperWithConsignee():List<ShipperWithConsignee>

}