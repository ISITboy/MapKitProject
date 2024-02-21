package com.example.mapkitresultproject.presentation.basecomponent.mapkit

import androidx.lifecycle.ViewModel
import com.example.mapkitresultproject.domain.state.SearchRouteState
import com.example.mapkitresultproject.domain.state.SearchState
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchOptions
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Queue

abstract class MapKitViewModel : ViewModel() {

    abstract fun setSearchOption(searchOptions: SearchOptions)
    abstract fun createSearchSession(query:String)
    abstract fun createSearchSession(query: Queue<String>)
    abstract fun setVisibleRegion(region: VisibleRegion?)
    abstract fun getSearchState() : MutableStateFlow<SearchState>


    abstract fun setDrivingOptions(drivingOptions : DrivingOptions)
    abstract fun setVehicleOptions(vehicleOptions: VehicleOptions)
    abstract fun setRouteTapListener(mapObjectTapListener: MapObjectTapListener)
    abstract fun createSessionCreateRoute()
    abstract fun getCreateRouteState() : MutableStateFlow<SearchRouteState>

}