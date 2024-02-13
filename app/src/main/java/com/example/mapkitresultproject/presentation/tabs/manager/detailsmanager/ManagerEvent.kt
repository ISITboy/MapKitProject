package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager

sealed class ManagerEvent{
    data object AddShipperItem:ManagerEvent()
    data object AddConsigneeItem:ManagerEvent()
}
