package com.example.mapkitresultproject.domain.usecase

import com.example.mapkitresultproject.data.remote.dto.distances.Query
import com.example.mapkitresultproject.data.repository.RouteRepository
import javax.inject.Inject

class CalculateRoutesUseCases @Inject constructor(
    private val routeRepository: RouteRepository

) {
    suspend fun invoke(body: Query)= routeRepository.calculateDistance(body=body)
}