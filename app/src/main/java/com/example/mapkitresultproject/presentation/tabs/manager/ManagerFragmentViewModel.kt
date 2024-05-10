package com.example.mapkitresultproject.presentation.tabs.manager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapkitresultproject.algorithm.ClarkeRightAlgorithm
import com.example.mapkitresultproject.algorithm.getKilometerWinningMatrix
import com.example.mapkitresultproject.algorithm.getShortedDistanceMatrix
import com.example.mapkitresultproject.data.remote.dto.distances.Query
import com.example.mapkitresultproject.domain.usecase.CalculateRoutesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.example.mapkitresultproject.algorithm.newAlgo.Clarke
import javax.inject.Inject


@HiltViewModel
class ManagerFragmentViewModel @Inject constructor(
    private val calculateRoutesUseCases: CalculateRoutesUseCases
):ViewModel() {


    private val _volumes : MutableLiveData<List<Double>> = MutableLiveData()
    val getVolumes : LiveData<List<Double>> = _volumes

    var count :Int = 0

    private var maxValueBus = MutableLiveData<Double>()
    fun getMaxValueBus():LiveData<Double>{
        return maxValueBus
    }

    private val clarkeRightItems:MutableLiveData<MutableList<ClarkeRightAlgorithm>> = MutableLiveData()
    val getClarkeRightItems:LiveData<MutableList<ClarkeRightAlgorithm>> = clarkeRightItems

    private val _addresses: MutableLiveData<List<Double>> = MutableLiveData()
    val addresses: LiveData<List<Double>> = _addresses

    private val _distances: MutableLiveData<List<List<Double>>> = MutableLiveData()
    val getDistances: LiveData<List<List<Double>>> = _distances

    fun setAddresses(addresses:List<Double>){
        _addresses.postValue(addresses)
    }

    fun calculateDistance(body: Query){
        viewModelScope.launch {
            _distances.value = calculateRoutesUseCases.invoke(body).data?.distances
        }
    }

    fun startClarkeRightAlgo(matrix: List<List<Double>>, count:Int){
        Log.d(TAG,"run")
        val qwe = matrix.map { it.toTypedArray() }.toTypedArray()
        val resultedMatrix = getShortedDistanceMatrix(qwe,2).map { makeMatrixWithoutGO(getKilometerWinningMatrix(it)) }
        val c = Clarke(resultedMatrix, mutableListOf(800.0, 800.0, 800.0, 800.0, 800.0, 800.0, 800.0, 800.0, 800.0, 800.0))
        c.setMaxTonnage(6000.0)
        c.startMethod()
        c.getRoutes().forEach {
            Log.d(TAG,"-> $it")
        }
    }
    fun makeMatrixWithoutGO(matrix: Array<Array<Double>>): Array<Array<Double>> {
        val curMatrix: Array<Array<Double>> = Array(matrix.size - 1) { Array(matrix.size - 1) { Int.MAX_VALUE.toDouble() } }
        for (i in curMatrix.indices) {
            for (j in 0 until curMatrix[i].size) {
                curMatrix[i][j] = matrix[i + 1][j + 1]
            }
        }
        return curMatrix
    }
    fun readMatrix(matrix: Array<Array<Double>>) {
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                print("${matrix[j][i]} \t")
            }
            println()
        }
    }
    companion object{
        const val TAG = "ManagerFragmentViewModel"
    }

    fun printMatrix(matrix : Array<Array<Double>>){
        for (row in matrix){
            for(column in row){
                print("$column \t")
            }
            println()
        }
    }
}