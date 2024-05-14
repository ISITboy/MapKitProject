package com.example.mapkitresultproject.domain.models

import androidx.compose.runtime.mutableStateOf
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mapkitresultproject.domain.Constants.TABLE_TRANSPORT
import com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager.compose.TransportDialogData

@Entity(tableName = TABLE_TRANSPORT)
data class Transport (
    @PrimaryKey(autoGenerate = true) val id :Int=0,
    @ColumnInfo(name = "name") val name : String,
    @ColumnInfo(name="volume") val volume:String,
    @ColumnInfo(name="height") val height:String? = null,
    @ColumnInfo(name="width") val width:String? = null,
    @ColumnInfo(name="length") val length:String? = null,
    @ColumnInfo(name="weight") val weight:String? = null
)

fun Transport.mapInTransportDialogData() =
    TransportDialogData(
        id = mutableStateOf(this.id),
        name = mutableStateOf(this.name),
        volume = mutableStateOf(this.volume),
        height = mutableStateOf(this.height ?: ""),
        width = mutableStateOf(this.width ?: ""),
        length = mutableStateOf(this.length ?: ""),
        weight = mutableStateOf(this.weight ?: "")
    )


