package com.example.mapkitresultproject.data.repository

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.domain.models.DrivingOptionsBuilder
import com.example.mapkitresultproject.domain.state.SearchRouteState
import com.example.mapkitresultproject.domain.models.VehicleOptionsBuilder
import com.example.mapkitresultproject.domain.repository.MapKitCreateRoutRepository
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MapKitCreateRoutRepositoryImpl @Inject constructor(
    private val context: Context
) : MapKitCreateRoutRepository {

    private val drivingRouter: DrivingRouter = DirectionsFactory.getInstance()
        .createDrivingRouter(DrivingRouterType.COMBINED)
    private var drivingOptions = DrivingOptionsBuilder().build()
    private var vehicleOptions = VehicleOptionsBuilder().build()
    private var session: DrivingSession? = null
    

    private var requestedPoints: MutableList<RequestPoint> = mutableListOf()

    private var routesCollection: MapObjectCollection? = null

    private val createRouteState = MutableStateFlow<SearchRouteState>(SearchRouteState.Off)

    private lateinit var routeTapListener: MapObjectTapListener

    private val polylinesMapsObjects: MutableList<PolylineMapObject?> = mutableListOf()

    override fun setRouteTapListener(mapObjectTapListener: MapObjectTapListener) {
        routeTapListener = mapObjectTapListener
    }

    override fun setDrivingOptions(drivingOptions : DrivingOptions) {
        this.drivingOptions = drivingOptions
    }

    override fun setVehicleOptions(vehicleOptions: VehicleOptions) {
        this.vehicleOptions = vehicleOptions
    }

    override fun createSessionCreateRoute() {
        if (requestedPoints.size > 1) {
            session = drivingRouter.requestRoutes(
                requestedPoints.takeLast(2),
                drivingOptions,
                vehicleOptions,
                drivingSessionCreateRouteListener
            )
            createRouteState.value = SearchRouteState.Loading
        }
    }

    private val drivingSessionCreateRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            val polyline = drivingRoutes.map { it.geometry }
            createRouteState.value = SearchRouteState.Success(drivingRoutes, polyline)
            createRouteState.value = SearchRouteState.Off

        }

        override fun onDrivingRoutesError(error: Error) {
            when (error) {
                is NetworkError -> createRouteState.value =
                    SearchRouteState.Error("Search request error due network issues")

                else -> createRouteState.value =
                    SearchRouteState.Error("Search request unknown error")

            }

        }
    }

    override fun setPointForRoute(point: Point) {
        this.requestedPoints.add(RequestPoint(point, RequestPointType.WAYPOINT, null, null))
    }

    override fun clearRoutes() {
        polylinesMapsObjects.forEach {
            if (it?.isValid == true) it.removeTapListener(routeTapListener)
        }
        polylinesMapsObjects.clear()
        clearRoutesCollection()
        requestedPoints.clear()
    }

    override fun getCreateRouteState() = createRouteState
    override fun setMapObjectRoutesCollection(mapObjectCollection: MapObjectCollection) {
        routesCollection = mapObjectCollection.addCollection()
    }

    override fun onRoutesUpdated(routes: List<DrivingRoute>) {
        routes.forEachIndexed { index, route ->
            this.polylinesMapsObjects.add(routesCollection?.addPolyline(route.geometry)?.apply {
                addTapListener(routeTapListener)
                userData = route
                if (index == 0) styleMainRoute() else styleAlternativeRoute()
            })
        }
    }

    private fun PolylineMapObject.styleMainRoute() {
        zIndex = 10f
        setStrokeColor(ContextCompat.getColor(context, R.color.gray))
        strokeWidth = 5f
        outlineColor = ContextCompat.getColor(context, R.color.black)
        outlineWidth = 3f
    }

    private fun PolylineMapObject.styleAlternativeRoute() {
        zIndex = 5f
        setStrokeColor(ContextCompat.getColor(context, R.color.light_blue))
        strokeWidth = 4f
        outlineColor = ContextCompat.getColor(context, R.color.black)
        outlineWidth = 2f
    }

    override fun getPolylinesMapsObjects() = polylinesMapsObjects
    private fun clearRoutesCollection() {
        routesCollection?.clear()
    }
}
