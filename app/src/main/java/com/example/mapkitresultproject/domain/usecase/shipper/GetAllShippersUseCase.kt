package com.example.mapkitresultproject.domain.usecase.shipper

import com.example.mapkitresultproject.domain.repository.ShipperRepository
import javax.inject.Inject

class GetAllShippersUseCase @Inject constructor(
    private val shipperRepository: ShipperRepository
) {
    operator fun invoke() = shipperRepository.getAllShippers()
}