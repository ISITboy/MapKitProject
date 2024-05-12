package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager

import com.example.mapkitresultproject.domain.models.Consignee
import com.example.mapkitresultproject.domain.models.Shipper

sealed class MembersEvent{
    data class AddShipperItem(val shipper: Shipper):MembersEvent()
    data class AddConsigneeItem(val consignee:Consignee):MembersEvent()
    data class DeleteConsigneeItem(val consignee:Consignee):MembersEvent()
    data class DeleteShipperItem(val shipper: Shipper):MembersEvent()
}

sealed class DialogEvent{
    data object OpenConsigneeDialog:DialogEvent()
    data object OpenShipperDialog:DialogEvent()
    data object HideDialog:DialogEvent()
}

