package com.example.mapkitresultproject.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mapkitresultproject.domain.models.Consignee
import com.example.mapkitresultproject.domain.models.Shipper

@Database(entities = [Consignee::class, Shipper::class],version = 1,)
abstract class Database :RoomDatabase(){
    abstract val shipperDao: ShipperDao
    abstract val consigneeDao: ConsigneeDao
}