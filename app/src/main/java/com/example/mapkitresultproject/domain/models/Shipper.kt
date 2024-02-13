package com.example.mapkitresultproject.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mapkitresultproject.domain.Constants.TABLE_SHIPPER

@Entity(tableName = TABLE_SHIPPER)
data class Shipper(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    @ColumnInfo(name = "address") val address:String,
)