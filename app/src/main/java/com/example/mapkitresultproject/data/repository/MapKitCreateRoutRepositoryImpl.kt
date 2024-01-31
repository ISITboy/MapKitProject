package com.example.mapkitresultproject.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.mapkitresultproject.domain.models.SearchState
import com.example.mapkitresultproject.domain.repository.MapKitCreateRoutRepository
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.Point
import com.yandex.runtime.Error
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MapKitCreateRoutRepositoryImpl @Inject constructor() : MapKitCreateRoutRepository {

    private lateinit var drivingRouter: DrivingRouter
    private lateinit var drivingOptions: DrivingOptions
    private lateinit var vehicleOptions: VehicleOptions
    private lateinit var points: List<RequestPoint>

    private var resultResponse = MutableLiveData<List<Point>>()
    private var session: DrivingSession? = null
    private val createRouteState = MutableStateFlow<SearchState>(SearchState.Off)


    override fun setDrivingRouter(drivingRouter: DrivingRouter) {
        this.drivingRouter = drivingRouter
    }

    override fun setDrivingOptions(
        routesCount: Int,
        avoidTolls: Boolean,
        avoidPoorConditions: Boolean,
        avoidUnpaved: Boolean
    ) {
        drivingOptions = DrivingOptions().apply {
            this.routesCount = routesCount
            this.avoidTolls = avoidTolls
            this.avoidPoorConditions = avoidPoorConditions
            this.avoidUnpaved = avoidUnpaved
        }
    }

    override fun setVehicleOptions(vehicleType: VehicleType, weight: Float) {
        vehicleOptions = VehicleOptions().apply {
            this.vehicleType = vehicleType
            this.weight = weight
        }

    }

    override fun createSessionCreateRoute() {
        session = drivingRouter.requestRoutes(
            points,
            drivingOptions,
            vehicleOptions,
            drivingSessionCreateRouteListener
        )
        createRouteState.value = SearchState.Loading
    }

    private val drivingSessionCreateRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            resultResponse.value = drivingRoutes.flatMap { point -> point.geometry.points }
            createRouteState.value = SearchState.Off
        }

        override fun onDrivingRoutesError(error: Error) {
            createRouteState.value = SearchState.Error(message = "DrivingRoutesError")
        }
    }

    override fun setPointsForRoute(points: List<Point>) {
        this.points = buildList {
            addAll(points.map {
                RequestPoint(it, RequestPointType.VIAPOINT, null, null)
            })
        }
    }

    override fun clearPointsForRoute() {
        points = listOf()
    }

    override fun getCreateRouteState() = createRouteState.value

    override fun getResultedRout() = resultResponse

}