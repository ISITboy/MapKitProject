package com.example.mapkitresultproject.domain.usecase.shipper

import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.domain.repository.ShipperRepository
import javax.inject.Inject

class UpdateShipperUseCase @Inject constructor(
    private val shipperRepository: ShipperRepository
) {
    suspend operator fun invoke(shipper: Shipper) {
        shipperRepository.updateShipper(shipper)
    }
}