package com.example.mapkitresultproject.domain.usecase.mapkitusescases

import com.example.mapkitresultproject.domain.repository.MapKitCreateRoutRepository
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import javax.inject.Inject

class CreateRouteUseCase @Inject constructor(
    private val createRoutRepository: MapKitCreateRoutRepository
) {
    fun setDrivingOptions(drivingOptions : DrivingOptions) {
        createRoutRepository.setDrivingOptions(drivingOptions = drivingOptions)
    }

    fun setVehicleOptions(vehicleOptions: VehicleOptions) {
        createRoutRepository.setVehicleOptions(vehicleOptions = vehicleOptions)
    }

    fun createSessionCreateRoute() {
        createRoutRepository.createSessionCreateRoute()
    }
    fun getPolylinesMapsObjects() = createRoutRepository.getPolylinesMapsObjects()


    fun getCreateRouteState() = createRoutRepository.getCreateRouteState()

    fun setMapObjectRoutesCollection(mapObjectCollection: MapObjectCollection){
        createRoutRepository.setMapObjectRoutesCollection(mapObjectCollection = mapObjectCollection)
    }
    fun setRouteTapListener(mapObjectTapListener: MapObjectTapListener){
        createRoutRepository.setRouteTapListener(mapObjectTapListener)
    }
    fun clearRoutes(){
        createRoutRepository.clearRoutes()
    }

    fun onRoutesUpdated(routes: List<DrivingRoute>) {
        createRoutRepository.onRoutesUpdated(routes = routes)
    }

    fun setPointForRoute(point: Point){
        createRoutRepository.setPointForRoute(point=point)
    }

}