package com.example.mapkitresultproject.domain.models

import androidx.room.Embedded
import androidx.room.Relation

data class ShipperWithConsignee (
    @Embedded val shipper: Shipper,
    @Relation(parentColumn = "id", entityColumn = "shipper_id")
    val consignees:List<Consignee>
)