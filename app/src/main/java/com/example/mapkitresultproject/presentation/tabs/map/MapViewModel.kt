package com.example.mapkitresultproject.presentation.tabs.map

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.mapkitresultproject.domain.usecase.mapkitusescases.CreateRouteUseCase
import com.example.mapkitresultproject.domain.usecase.mapkitusescases.InteractionUseCase
import com.example.mapkitresultproject.domain.usecase.mapkitusescases.SearchUseCase
import com.yandex.mapkit.Animation
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val searchUseCase: SearchUseCase,
    private val createRouteUseCase: CreateRouteUseCase,
    private val interactionUseCase: InteractionUseCase
) : ViewModel() {


    lateinit var map: Map


//    Start SearchRepository Dependency
    fun setVisibleRegion(region: VisibleRegion?) {
        searchUseCase.setVisibleRegion(region)
    }

    fun setSearchOption(searchOptions: SearchOptions) {
        searchUseCase.setSearchOption(searchOptions = searchOptions)
    }

    fun createSession(query: String) = searchUseCase.createSession(query = query)

    fun setMapObjectTapListener(mapObjectTapListener: MapObjectTapListener){
        searchUseCase.setMapObjectTapListener(mapObjectTapListener)
    }
    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection) {
        searchUseCase.setMapObjectCollection(mapObjectCollection)
    }

    fun clearObjectCollection(){
        searchUseCase.clearObjectCollection()
    }
    fun getSearchState() = searchUseCase.getSearchState()

    fun subscribeForSearch() = searchUseCase.subscribeForSearch()


    //    Start CreateRouteRepository Dependency
    fun setDrivingOptions(drivingOptions : DrivingOptions) {
        createRouteUseCase.setDrivingOptions(drivingOptions = drivingOptions)
    }

    fun setVehicleOptions(vehicleOptions: VehicleOptions) {
        createRouteUseCase.setVehicleOptions(vehicleOptions = vehicleOptions)
    }

    fun setRouteTapListener(mapObjectTapListener:MapObjectTapListener){
        createRouteUseCase.setRouteTapListener(mapObjectTapListener)
    }

    fun createSessionCreateRoute() {
        createRouteUseCase.createSessionCreateRoute()
    }

    fun clearRoutes() {
        createRouteUseCase.clearRoutes()
    }
    fun getPolylinesMapsObjects() = createRouteUseCase.getPolylinesMapsObjects()


    fun getCreateRouteState() = createRouteUseCase.getCreateRouteState()


    fun setMapObjectRoutesCollection(mapObjectCollection: MapObjectCollection){
        createRouteUseCase.setMapObjectRoutesCollection(mapObjectCollection = mapObjectCollection)
    }
    fun onRoutesUpdated(routes: List<DrivingRoute>){
        createRouteUseCase.onRoutesUpdated(routes = routes)
    }
    fun setPointForRoute(point: Point) {
        createRouteUseCase.setPointForRoute(point = point)
    }

    //    Start InteractionRepository Dependency
    fun addMapInputListener(map: Map) = interactionUseCase.addMapInputListener(map)

    fun getSelectedPoint() = interactionUseCase.getSelectedPoint()

    fun setMapObjectPlacemarksCollection(mapObjectCollection: MapObjectCollection) {
        interactionUseCase.setMapObjectPlacemarksCollection(mapObjectCollection)
    }

    fun clearSelectedPointsForCreateRoute(){
        interactionUseCase.clearSelectedPointsForCreateRoute()
    }



    fun focusCamera(points: List<Point>, boundingBox: BoundingBox) {
        if (points.isEmpty()) return

        val position = if (points.size == 1) {
            map.cameraPosition.run {
                CameraPosition(points.first(), 15f, azimuth, tilt)
            }
        } else {
            map.cameraPosition(Geometry.fromBoundingBox(boundingBox))
        }

        map.move(position, Animation(Animation.Type.SMOOTH, 0.5f), null)
    }
    fun focusCamera(points: List<Point>, polyline: Polyline) {
        if (points.isEmpty()) return

        val position = if (points.size == 1) {
            map.cameraPosition.run {
                CameraPosition(points.first(), zoom, azimuth, tilt)
            }
        } else {
            map.cameraPosition(Geometry.fromPolyline(polyline))
        }

        map.move(position, Animation(Animation.Type.SMOOTH, 0.5f), null)
    }

}