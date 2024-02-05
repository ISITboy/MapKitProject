package com.example.mapkitresultproject.domain.usecase

import com.example.mapkitresultproject.domain.repository.MapKitInteractionMapRepository
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import javax.inject.Inject

class InteractionUseCase @Inject constructor(
    private val interactionMapRepository: MapKitInteractionMapRepository
) {

    fun addMapInputListener(map:Map) = interactionMapRepository.addMapInputListener(map)
    fun getSelectedPoint() = interactionMapRepository.getSelectedPoint()
    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection) = interactionMapRepository.setMapObjectCollection(
        mapObjectCollection = mapObjectCollection
    )
}