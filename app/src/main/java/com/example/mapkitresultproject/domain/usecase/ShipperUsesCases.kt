package com.example.mapkitresultproject.domain.usecase

import com.example.mapkitresultproject.domain.usecase.shipper.DeleteShipperUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.GetAllShippersUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.GetShipperUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.InsertShipperUseCase
import com.example.mapkitresultproject.domain.usecase.shipper.UpdateShipperUseCase
import javax.inject.Inject

data class ShipperUsesCases @Inject constructor(
    val deleteShipperUseCase: DeleteShipperUseCase,
    val insertShipperUseCase: InsertShipperUseCase,
    val getAllShipperUseCase: GetAllShippersUseCase,
    val getShipperUseCase: GetShipperUseCase,
    val updateShipperUseCase: UpdateShipperUseCase
)