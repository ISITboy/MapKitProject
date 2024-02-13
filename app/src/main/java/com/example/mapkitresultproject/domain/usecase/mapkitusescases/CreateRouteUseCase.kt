package com.example.mapkitresultproject.domain.usecase.mapkitusescases

import com.example.mapkitresultproject.domain.repository.MapKitCreateRoutRepository
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import javax.inject.Inject

class CreateRouteUseCase @Inject constructor(
    private val createRoutRepository: MapKitCreateRoutRepository
) {
    fun setDrivingRouter(drivingRouter: DrivingRouter) {
        createRoutRepository.setDrivingRouter(
            drivingRouter = drivingRouter
        )
    }

    fun setDrivingOptions(
        routesCount: Int,
        avoidTolls: Boolean,
        avoidPoorConditions: Boolean,
        avoidUnpaved: Boolean
    ) {
        createRoutRepository.setDrivingOptions(
            routesCount = routesCount,
            avoidTolls = avoidTolls,
            avoidPoorConditions = avoidPoorConditions,
            avoidUnpaved = avoidUnpaved
        )
    }

    fun setVehicleOptions(vehicleType: VehicleType, weight: Float) {
        createRoutRepository.setVehicleOptions(
            vehicleType = vehicleType, weight = weight
        )
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

    fun onRoutesUpdated(map: Map, routes: List<DrivingRoute>) {
        createRoutRepository.onRoutesUpdated(map,routes = routes)
    }

    fun setPointForRoute(point: Point){
        createRoutRepository.setPointForRoute(point=point)
    }

}