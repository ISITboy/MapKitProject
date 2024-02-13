package com.example.mapkitresultproject.presentation.tabs.manager.detailsmanager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapkitresultproject.domain.models.Consignee
import com.example.mapkitresultproject.domain.models.Shipper
import com.example.mapkitresultproject.domain.usecase.ConsigneeUsesCases
import com.example.mapkitresultproject.domain.usecase.ShipperUsesCases
import com.example.mapkitresultproject.domain.usecase.mapkitusescases.SearchUseCase
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
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
) : ViewModel() {
    fun setSearchOption(searchOptions: SearchOptions){
        searchUseCase.setSearchOption(searchOptions = searchOptions)
    }
    fun createSearchSession(query:String){
        searchUseCase.createSession(query)
    }

    fun createSession(query: Queue<String>) {
        searchUseCase.createSession(query = query)
    }
    fun setVisibleRegion(){
        searchUseCase.setVisibleRegion(VisibleRegion(Point(53.768729, 23.620159),Point(53.770823, 24.023757),Point(53.589836, 23.671409),Point(53.587338, 23.975378)))
    }
    fun getSearchState() = searchUseCase.getSearchState()



    private val shipperItems = MutableLiveData<List<Shipper>>()
    fun getShipper(): LiveData<List<Shipper>> {
        return shipperItems
    }

    private val consigneeItems = MutableLiveData<List<Consignee>>()
    fun getConsignee(): LiveData<List<Consignee>> {
        return consigneeItems
    }

    val event = MutableStateFlow<ManagerEvent>(ManagerEvent.AddShipperItem)
    fun insertShipper(shipper: Shipper) = viewModelScope.launch {
        shipperUsesCases.insertShipperUseCase(shipper = shipper)
    }

    fun insertConsignee(consignee: Consignee) = viewModelScope.launch {
        consigneeUsesCases.insertConsigneeUseCase(consignee = consignee)
    }

    fun deleteShipper(shipper: Shipper) = viewModelScope.launch {
        shipperUsesCases.deleteShipperUseCase(shipper = shipper)
    }


    fun deleteConsignee(consignee: Consignee) = viewModelScope.launch {
        consigneeUsesCases.deleteConsigneeUseCase(consignee = consignee)
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
}