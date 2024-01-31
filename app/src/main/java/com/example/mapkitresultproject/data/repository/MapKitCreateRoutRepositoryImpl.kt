package com.example.mapkitresultproject.data.repository

import android.content.Context
import android.util.Log
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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MapKitCreateRoutRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : MapKitCreateRoutRepository {

    private lateinit var drivingRouter: DrivingRouter
    private lateinit var drivingOptions: DrivingOptions
    private lateinit var vehicleOptions: VehicleOptions
    private var session: DrivingSession? = null


    private lateinit var points: List<RequestPoint>
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
        this.points = buildList {
            addAll(points.map {
                RequestPoint(it, RequestPointType.WAYPOINT, null, null)
            })
        }
    }

    override fun clearPointsForRoute() {
        points = listOf()
    }

    override fun getCreateRouteState() = createRouteState.value

    override fun getResultedRout() = resultResponse

    fun setMapObjectCollection(mapObjectCollection:MapObjectCollection){
        routesCollection = mapObjectCollection
    }

    fun onRoutesUpdated(routes: List<DrivingRoute>) {
        clearRoutesCollection()
        routes.forEachIndexed { index, route ->
            routesCollection?.addPolyline(route.geometry)?.apply {
                if (index == 0) styleMainRoute() else styleAlternativeRoute()
            }
        }
    }

    fun PolylineMapObject.styleMainRoute() {
        zIndex = 10f
        setStrokeColor(ContextCompat.getColor(appContext, R.color.gray))
        strokeWidth = 5f
        outlineColor = ContextCompat.getColor(appContext, R.color.black )
        outlineWidth = 3f
    }

    fun PolylineMapObject.styleAlternativeRoute() {
        zIndex = 5f
        setStrokeColor(ContextCompat.getColor(appContext, R.color.light_blue))
        strokeWidth = 4f
        outlineColor = ContextCompat.getColor(appContext, R.color.black)
        outlineWidth = 2f
    }

    private fun clearRoutesCollection() {
        routesCollection?.clear()
    }
}