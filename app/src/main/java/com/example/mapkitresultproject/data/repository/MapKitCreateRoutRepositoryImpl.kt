package com.example.mapkitresultproject.data.repository

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.mapkitresultproject.R
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
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MapKitCreateRoutRepositoryImpl @Inject constructor(
    private val context: Context
) : MapKitCreateRoutRepository {

    private lateinit var drivingRouter: DrivingRouter
    private lateinit var drivingOptions: DrivingOptions
    private lateinit var vehicleOptions: VehicleOptions
    private var session: DrivingSession? = null


    private var pointsForCreateRoute: MutableList<RequestPoint> = mutableListOf()
    private var resultResponse = MutableLiveData<List<DrivingRoute>>()

    private var routesCollection: MapObjectCollection?=null

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
        if(pointsForCreateRoute.size>1) {
            session = drivingRouter.requestRoutes(
                pointsForCreateRoute,
                drivingOptions,
                vehicleOptions,
                drivingSessionCreateRouteListener
            )
            createRouteState.value = SearchState.Loading
        }
    }

    private val drivingSessionCreateRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            resultResponse.value= drivingRoutes
            createRouteState.value = SearchState.Off

        }

        override fun onDrivingRoutesError(error: Error) {
            when (error) {
                is NetworkError -> (createRouteState.value as SearchState.Error).message = "Routes request error due network issues"
                else -> (createRouteState.value as SearchState.Error).message = "Routes request unknown error"
            }

        }
    }

    override fun setPointsForRoute(points: List<Point>) {
        this.pointsForCreateRoute = buildList {
            addAll(points.map {
                RequestPoint(it, RequestPointType.WAYPOINT, null, null)
            })
        }.toMutableList()
    }

    override fun setPointForRoute(point: Point) {
        this.pointsForCreateRoute.add(RequestPoint(point,RequestPointType.WAYPOINT, null, null))
    }

    override fun clearPointsForRoute() {
        pointsForCreateRoute.clear()
    }

    override fun getCreateRouteState() = createRouteState.value

    override fun getResultedRout() = resultResponse

    override fun setMapObjectCollection(mapObjectCollection:MapObjectCollection){
        routesCollection = mapObjectCollection
    }

    override fun onRoutesUpdated(routes: List<DrivingRoute>) {
        clearRoutesCollection()
        routes.forEachIndexed { index, route ->
            routesCollection?.addPolyline(route.geometry)?.apply {
                if (index == 0) styleMainRoute() else styleAlternativeRoute()
            }
        }
    }

    private fun PolylineMapObject.styleMainRoute() {
        zIndex = 10f
        setStrokeColor(ContextCompat.getColor(context, R.color.gray))
        strokeWidth = 5f
        outlineColor = ContextCompat.getColor(context, R.color.black )
        outlineWidth = 3f
    }

    private fun PolylineMapObject.styleAlternativeRoute() {
        zIndex = 5f
        setStrokeColor(ContextCompat.getColor(context, R.color.light_blue))
        strokeWidth = 4f
        outlineColor = ContextCompat.getColor(context, R.color.black)
        outlineWidth = 2f
    }

    private fun clearRoutesCollection() {
        routesCollection?.clear()
    }
}