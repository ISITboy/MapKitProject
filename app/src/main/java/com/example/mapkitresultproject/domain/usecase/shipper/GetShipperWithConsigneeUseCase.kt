package com.example.mapkitresultproject.domain.usecase.shipper

import com.example.mapkitresultproject.domain.repository.ShipperRepository
import javax.inject.Inject

class GetShipperWithConsigneeUseCase @Inject constructor(
    private val shipperRepository: ShipperRepository
) {
    suspend operator fun invoke() = shipperRepository.getShipperWithConsignee()
}