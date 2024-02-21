package com.example.mapkitresultproject.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mapkitresultproject.domain.Constants.TABLE_CONSIGNEE
import com.example.mapkitresultproject.domain.models.Consignee
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsigneeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(consignee: Consignee)
    @Delete
    suspend fun delete(consignee: Consignee)
    @Query("SELECT * FROM $TABLE_CONSIGNEE")
    fun getAllConsignee(): Flow<List<Consignee>>
    @Query("SELECT * FROM $TABLE_CONSIGNEE WHERE id=:id")
    suspend fun getConsignee(id: Int): Consignee?
}