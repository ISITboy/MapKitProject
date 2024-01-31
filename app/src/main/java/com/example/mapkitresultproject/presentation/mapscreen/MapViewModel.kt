package com.example.mapkitresultproject.presentation.mapscreen

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.mapkitresultproject.R
import com.example.mapkitresultproject.domain.usecase.CreateRouteUseCase
import com.example.mapkitresultproject.domain.usecase.SearchUseCase
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val searchUseCase: SearchUseCase,
    private val createRouteUseCase: CreateRouteUseCase
) : ViewModel() {

    init {
        setSearchManager(
            searchManager = SearchFactory.getInstance()
                .createSearchManager(SearchManagerType.COMBINED)
        )
        setDrivingRouter(
            drivingRouter = DirectionsFactory.getInstance()
                .createDrivingRouter(DrivingRouterType.COMBINED)
        )
    }

    lateinit var map: Map

    var placemarksCollection: MapObjectCollection?=null
    var routesCollection: MapObjectCollection?=null

    private fun setSearchManager(searchManager: SearchManager) {
        searchUseCase.setSearchManager(searchManager = searchManager)
    }

    fun setVisibleRegion(region: VisibleRegion) {
        searchUseCase.setVisibleRegion(region)
    }

    fun setSearchOption(resultPageSize: Int, searchTypes: SearchType) {
        searchUseCase.setSearchOption(
            resultPageSize = resultPageSize, searchTypes = searchTypes
        )
    }

    fun createSession(query: String, geometry: Geometry) = searchUseCase.createSession(
        query = query,
        geometry = geometry
    )

    fun getSearchState() = searchUseCase.getSearchState()

    fun getResultedPoint()= searchUseCase.getResultedPoint()

    private fun setDrivingRouter(drivingRouter: DrivingRouter) {
        createRouteUseCase.setDrivingRouter(drivingRouter = drivingRouter)
    }

    fun setDrivingOptions(
        routesCount: Int,
        avoidTolls: Boolean = true,
        avoidPoorConditions: Boolean = true,
        avoidUnpaved: Boolean = true
    ) {
        createRouteUseCase.setDrivingOptions(
            routesCount = routesCount,
            avoidTolls = avoidTolls,
            avoidPoorConditions = avoidPoorConditions,
            avoidUnpaved = avoidUnpaved
        )
    }

    fun setVehicleOptions(vehicleType: VehicleType, weight: Float) {
        createRouteUseCase.setVehicleOptions(
            vehicleType = vehicleType, weight = weight
        )
    }

    fun createSessionCreateRoute() {
        createRouteUseCase.createSessionCreateRoute()
    }

    fun setPointsForRoute(points: List<Point>) {
        createRouteUseCase.setPointsForRoute(points = points)
    }

    fun clearPointsForRoute() {
        createRouteUseCase.clearPointsForRoute()
    }

    fun getCreateRouteState() = createRouteUseCase.getCreateRouteState()

    fun getResultedRout() = createRouteUseCase.getResultedRout()




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

//    private val searchedPoints = SearchedPoints()
//

//
//    private var drivingSession: DrivingSession? = null
//    lateinit var placemarksCollection: MapObjectCollection
//    lateinit var routesCollection: MapObjectCollection
//
//    lateinit var drivingRouter: DrivingRouter
//
//
//    private val searchManager =
//        SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
//    var searchSession: Session? = null
//    private val searchOptions = SearchOptions()
//    private val region = MutableStateFlow<VisibleRegion?>(null)
//
//    val drivingOptions = DrivingOptions().apply {
//        routesCount = 1
//    }
//    val vehicleOptions = VehicleOptions().apply {
//        vehicleType = VehicleType.TRUCK
//    }
//    fun setVisibleRegion(region: VisibleRegion) {
//        this.region.value = region
//    }
//
//    var points :MutableLiveData< MutableList<Point>> = MutableLiveData()
//    private val listPoints = mutableListOf<Point>()
//
//
//    val searchSessionListener = object : Session.SearchListener {
//        override fun onSearchResponse(response: Response) {
//
//                val items = response.collection.children.mapNotNull {
//                    val point = it.obj?.geometry?.firstOrNull()?.point ?: return@mapNotNull null
//                    Log.d("MyLog", "latitude = ${point.latitude} longitude = ${point.longitude}")
//                    listPoints.add(point)
//                    if(listPoints.size>1)
//                        drivingRouterSummarySession(
//                            buildRequestPoints(listPoints.toList()),
//                            drivingOptions,
//                            vehicleOptions
//                        ) else return@mapNotNull null
//                }
//
//        }
//
//        override fun onSearchError(p0: Error) {
//            Log.d("MyLog", "Error = ${p0.toString()}")
//        }
//
//    }
//
//    fun submitSearch(query:String) {
//        searchSession = searchManager.submit(
//            query,
//            VisibleRegionUtils.toPolygon(map.visibleRegion),
//            SearchOptions().apply {
//                resultPageSize = 1
//            },
//            searchSessionListener
//        )
//    }
//
//
//
//    var routePoints = emptyList<Point>()
//        set(value) {
//            field = value
//            onRoutePointsUpdated()
//        }
//
//    private var routes = emptyList<DrivingRoute>()
//        set(value) {
//            field = value
//            onRoutesUpdated()
//        }
//
//    private fun onRoutePointsUpdated() {
//        placemarksCollection.clear()
//
//
//
//        if (routePoints.isEmpty()) {
//            drivingSession?.cancel()
//            routes = emptyList()
//            return
//        }
//
//        val imageProvider = ImageProvider.fromResource(appContext, R.drawable.bullet)
//        routePoints.forEach {
//            placemarksCollection.addPlacemark(
//                it,
//                imageProvider,
//                IconStyle().apply {
//                    scale = 0.5f
//                    zIndex = 20f
//                }
//            )
//
//
//        }
//
//        if (routePoints.size < 2) return
//
//        val requestPoints = buildRequestPoints(routePoints)
//
//
//
//        drivingSession = drivingRouter.requestRoutes(
//            requestPoints,
//            drivingOptions,
//            vehicleOptions,
//            drivingRouteListener,
//        )
//        drivingRouterSummarySession(requestPoints,drivingOptions,vehicleOptions)
//    }
//
//    private fun buildRequestPoints(routePoints: List<Point>): List<RequestPoint> {
//        val requestPoints = buildList {
//            add(RequestPoint(routePoints.first(), RequestPointType.WAYPOINT, null, null))
//            addAll(
//                routePoints.subList(1, routePoints.size - 1)
//                    .map { RequestPoint(it, RequestPointType.VIAPOINT, null, null) })
//            add(RequestPoint(routePoints.last(), RequestPointType.WAYPOINT, null, null))
//        }
//        return requestPoints
//    }
//
//    private fun drivingRouterSummarySession(
//        requestPoints: List<RequestPoint>,
//        drivingOptions: DrivingOptions,
//        vehicleOptions: VehicleOptions
//    ) {
//
//            drivingRouter.requestRoutesSummary(
//                requestPoints,
//                drivingOptions,
//                vehicleOptions,
//                drivingRouteSummaryListener
//            )
//
//    }
//
//    private val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
//        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
//            Log.d(
//                "MyLog",
//                "drivingRoutes = ${drivingRoutes.first().metadata.weight.distance.value}"
//            )
//            routes = drivingRoutes
//        }
//
//        override fun onDrivingRoutesError(error: Error) {
//            when (error) {
//                is NetworkError -> Toast.makeText(
//                    appContext, "Routes request error due network issues",
//                    Toast.LENGTH_SHORT
//                ).show()
//
//                else -> Toast.makeText(
//                    appContext,
//                    "Routes request unknown error",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//    private val drivingRouteSummaryListener =
//        object : DrivingSummarySession.DrivingSummaryListener {
//            override fun onDrivingSummaries(p0: MutableList<Summary>) {
//                Log.d("MyLog", "Summary = ${p0.first().weight.distance.value}")
//                Log.d("MyLog", "Summary = ${p0.first().weight.distance.text}")
//            }
//
//            override fun onDrivingSummariesError(error: Error) {
//                when (error) {
//                    is NetworkError -> Toast.makeText(
//                        appContext, "Routes request error due network issues",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                    else -> Toast.makeText(
//                        appContext,
//                        "Routes request unknown error",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//
//        }
//
//    private fun onRoutesUpdated() {
//        routesCollection.clear()
//
//
//        if (routes.isEmpty()) return
//
//        routes.forEachIndexed { index, route ->
//            routesCollection.addPolyline(route.geometry).apply {
//                if (index == 0) styleMainRoute() else styleAlternativeRoute()
//            }
//        }
//    }
//
//    private fun PolylineMapObject.styleMainRoute() {
//        zIndex = 10f
//        setStrokeColor(ContextCompat.getColor(appContext, R.color.gray))
//        strokeWidth = 5f
//        outlineColor = ContextCompat.getColor(appContext, R.color.black)
//        outlineWidth = 3f
//    }
//
//    private fun PolylineMapObject.styleAlternativeRoute() {
//        zIndex = 5f
//        setStrokeColor(ContextCompat.getColor(appContext, R.color.light_blue))
//        strokeWidth = 4f
//        outlineColor = ContextCompat.getColor(appContext, R.color.black)
//        outlineWidth = 2f
//    }
//
//    fun moveToStartLocation(startLocation: StartLocation = StartLocation()) =
//        with(startLocation) {
//            map.move(
//                CameraPosition(location, zoomValue, azimuthValue, tiltValue),
//                Animation(Animation.Type.SMOOTH, 2f), null
//            )
//        }
//
//    val inputListener = object : InputListener {
//        override fun onMapTap(map: Map, point: Point) = Unit
//
//        override fun onMapLongTap(map: Map, point: Point) {
//            routePoints = routePoints + point
//        }
//    }

}