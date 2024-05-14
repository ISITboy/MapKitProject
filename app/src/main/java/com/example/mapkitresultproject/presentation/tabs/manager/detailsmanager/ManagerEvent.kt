package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager

import com.example.mapkitresultproject.domain.models.Consignee
import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.domain.models.Transport

sealed class MembersEvent{
    data class AddShipperItem(val shipper: Shipper):MembersEvent()
    data class AddConsigneeItem(val consignee:Consignee):MembersEvent()
    data class DeleteConsigneeItem(val consignee:Consignee):MembersEvent()
    data class DeleteShipperItem(val shipper: Shipper):MembersEvent()
    data class UpdateConsigneeItem(val consignee:Consignee):MembersEvent()
    data class UpdateShipperItem(val shipper: Shipper):MembersEvent()
    data class AddTransportItem(val transport: Transport):MembersEvent()
    data class DeleteTransportItem(val transport: Transport):MembersEvent()
    data class UpdateTransportItem(val transport: Transport):MembersEvent()
}

sealed class DialogEvent{
    data object OpenConsigneeDialog:DialogEvent()
    data object OpenShipperDialog:DialogEvent()
    data object HideMembersDialog:DialogEvent()
    data object HideTransportDialog:DialogEvent()
    data class OpenTransportDialog(val transport: Transport?) : DialogEvent()
}


