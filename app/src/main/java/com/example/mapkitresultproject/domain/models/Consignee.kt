package com.example.mapkitresultproject.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mapkitresultproject.domain.Constants.TABLE_CONSIGNEE


@Entity(tableName = TABLE_CONSIGNEE)
data class Consignee(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    @ColumnInfo(name = "address") val address:String,
    @ColumnInfo(name = "cargo_volume") val volume:Double,
    @ColumnInfo(name = "queue_number") val number:Int?=null,
    @ColumnInfo(name = "shipper_id") val shipper:Int?=null
)
