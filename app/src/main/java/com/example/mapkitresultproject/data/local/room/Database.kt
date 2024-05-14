package com.example.mapkitresultproject.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mapkitresultproject.domain.models.Consignee
import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.domain.models.Transport

@Database(entities = [Consignee::class, Shipper::class,Transport::class],version = 4)
abstract class Database :RoomDatabase(){
    abstract val shipperDao: ShipperDao
    abstract val consigneeDao: ConsigneeDao
    abstract val transportDao: TransportDao
}