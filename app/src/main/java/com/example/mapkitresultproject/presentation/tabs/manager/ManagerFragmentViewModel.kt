package com.example.mapkitresultproject.presentation.tabs.manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapkitresultproject.algorithm.ClarkeRightAlgorithm
import com.example.mapkitresultproject.algorithm.DataForClarkeRight
import com.example.mapkitresultproject.algorithm.getKilometerWinningMatrix
import com.example.mapkitresultproject.algorithm.getListKilometerWinningMatrix
import com.example.mapkitresultproject.algorithm.getShortestPathMatrix
import com.example.mapkitresultproject.data.remote.dto.distances.Query
import com.example.mapkitresultproject.domain.usecase.CalculateRoutesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ManagerFragmentViewModel @Inject constructor(
    private val calculateRoutesUseCases: CalculateRoutesUseCases
):ViewModel() {
    private val _volumes : MutableLiveData<List<Double>> = MutableLiveData()
    val getVolumes : LiveData<List<Double>> = _volumes

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

    fun startClarkeRightAlgorithm(listWithDistance: Array<Array<Double>>, countShipper:Int){
        viewModelScope.launch {
            val resultArray = getListKilometerWinningMatrix(
                0 until countShipper,
                getShortestPathMatrix(listWithDistance)
            )
            val kilometersWinMatrix = mutableListOf<MutableList<MutableList<Double>>>()
            resultArray.forEach {
                kilometersWinMatrix.add(getKilometerWinningMatrix(it.map { it.toTypedArray() }
                    .toTypedArray()))
            }
            val volumes = getVolumes
            val maxValue = maxValueBus

            val listItem = mutableListOf<ClarkeRightAlgorithm>()
            kilometersWinMatrix.forEach {
                listItem.add(
                    ClarkeRightAlgorithm(
                        DataForClarkeRight(
                            matrix = makeMatrixWithoutGO(it).map { it.toMutableList() }.toMutableList(),
                            cargoVolumes = volumes.value!!,
                            carrying = maxValue.value!!
                        )
                    )
                )
            }
            var checkDeleteWeight = ArrayList<Int>()
            while (listItem.first().cargoVolumes.max() != 0.0) {
                listItem.forEachIndexed { i, v ->
                    if (findItemWithMaxValue(listItem) == i) {
                        v.finish = false
                        v.start()
                        v.cargoVolumes.forEachIndexed { index, d ->
                            if (d == 0.0) {
                                checkDeleteWeight.add(index)
                            }
                        }
                    }
                }
                checkDeleteWeight.forEach { n ->
                    listItem.forEach {
                        it.putNullInMatrix(n)
                    }
                }
            }

            clarkeRightItems.value = listItem
        }
    }

    private fun makeMatrixWithoutGO(matrix: MutableList<MutableList<Double>>): Array<Array<Double>> {
        val curMatrix: Array<Array<Double>> = Array(matrix.size - 1) { Array(matrix.size - 1) { Int.MAX_VALUE.toDouble() } }
        for (i in curMatrix.indices) {
            for (j in 0 until curMatrix[i].size) {
                curMatrix[i][j] = matrix[i + 1][j + 1]
            }
        }
        return curMatrix
    }

    private fun findItemWithMaxValue(listItem: MutableList<ClarkeRightAlgorithm>): Int {
        var list = mutableListOf<Double>()
        for (i in listItem) {
            list.add(i.returnCheckMaxValues())
        }
        return list.withIndex().maxBy { it.value }.index
    }

}