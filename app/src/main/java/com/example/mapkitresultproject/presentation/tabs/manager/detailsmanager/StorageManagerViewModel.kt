package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mapkitresultproject.domain.models.Consignee
import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.domain.state.SearchRouteState
import com.example.mapkitresultproject.domain.state.SearchState
import com.example.mapkitresultproject.domain.usecase.ConsigneeUsesCases
import com.example.mapkitresultproject.domain.usecase.ShipperUsesCases
import com.example.mapkitresultproject.domain.usecase.mapkitusescases.SearchUseCase
import com.example.mapkitresultproject.presentation.basecomponent.mapkit.MapKitViewModel
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Queue
import javax.inject.Inject

@HiltViewModel
class StorageManagerViewModel @Inject constructor(
    private val consigneeUsesCases: ConsigneeUsesCases,
    private val shipperUsesCases: ShipperUsesCases,
    private val searchUseCase: SearchUseCase
) : MapKitViewModel() {

    private val shipperItems = MutableLiveData<List<Shipper>>()
    fun getShipper(): LiveData<List<Shipper>> {
        return shipperItems
    }

    private val consigneeItems = MutableLiveData<List<Consignee>>()
    fun getConsignee(): LiveData<List<Consignee>> {
        return consigneeItems
    }


    private fun insertShipper(shipper: Shipper) = viewModelScope.launch {
        shipperUsesCases.insertShipperUseCase(shipper = shipper)
    }

    private fun insertConsignee(consignee: Consignee) = viewModelScope.launch {
        consigneeUsesCases.insertConsigneeUseCase(consignee = consignee)
    }

    private fun deleteShipper(shipper: Shipper) = viewModelScope.launch {
        shipperUsesCases.deleteShipperUseCase(shipper = shipper)
    }


    private fun deleteConsignee(consignee: Consignee) = viewModelScope.launch {
        consigneeUsesCases.deleteConsigneeUseCase(consignee = consignee)
    }

    fun eventManager(event: MembersEvent){
        when(event){
            is MembersEvent.AddConsigneeItem -> insertConsignee(event.consignee)
            is MembersEvent.AddShipperItem -> insertShipper(event.shipper)
            is MembersEvent.DeleteConsigneeItem -> deleteConsignee(event.consignee)
            is MembersEvent.DeleteShipperItem -> deleteShipper(event.shipper)
        }
    }

    fun getAllShipperItems() = viewModelScope.launch {
        shipperUsesCases.getAllShipperUseCase().collect {
            shipperItems.postValue(it)
            Log.d("MyLog","sizeM: ${shipperItems.value?.size}")
        }
    }

    fun getAllConsigneeItems() = viewModelScope.launch {
        consigneeUsesCases.getAllConsigneeUseCase().collect {
            consigneeItems.postValue(it)
            Log.d("MyLog","sizeM: ${consigneeItems.value?.size}")
        }
    }

    override fun setSearchOption(searchOptions: SearchOptions) {
        searchUseCase.setSearchOption(searchOptions = searchOptions)
    }

    override fun createSearchSession(query: String) {
        searchUseCase.createSession(query)
    }

    override fun createSearchSession(query: Queue<String>) {
        searchUseCase.createSession(query)
    }

    override fun setVisibleRegion(region: VisibleRegion?) {
        searchUseCase.setVisibleRegion(VisibleRegion(Point(53.768729, 23.620159),Point(53.770823, 24.023757),Point(53.589836, 23.671409),Point(53.587338, 23.975378)))
    }

    override fun getSearchState(): MutableStateFlow<SearchState> = searchUseCase.getSearchState()


    override fun setDrivingOptions(drivingOptions: DrivingOptions) {
        TODO("Not yet implemented")
    }

    override fun setVehicleOptions(vehicleOptions: VehicleOptions) {
        TODO("Not yet implemented")
    }

    override fun setRouteTapListener(mapObjectTapListener: MapObjectTapListener) {
        TODO("Not yet implemented")
    }

    override fun createSessionCreateRoute() {
        TODO("Not yet implemented")
    }

    override fun getCreateRouteState(): MutableStateFlow<SearchRouteState> {
        TODO("Not yet implemented")
    }
}