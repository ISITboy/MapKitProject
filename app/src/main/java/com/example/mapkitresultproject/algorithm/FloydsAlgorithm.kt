package com.example.mapkitresultproject.algorithm

import com.example.mapkitresultproject.algorithm.utils.transposeMatrix
import java.math.RoundingMode

private fun floydsAlgorithm(distanceMatrix: Array<Array<Double>>): Array<Array<Double>> {
    for (k in distanceMatrix.indices) {
        for (i in distanceMatrix.indices) {
            for (j in distanceMatrix.indices) {
                if ((distanceMatrix[i][j] > distanceMatrix[i][k] + distanceMatrix[k][j]) && distanceMatrix[i][k] < Int.MAX_VALUE && distanceMatrix[k][j] < Int.MAX_VALUE) {
                    distanceMatrix[i][j] = formattingNumber(distanceMatrix[i][k] + distanceMatrix[k][j])
                }
            }
        }
    }
    return distanceMatrix
}

private fun formattingNumber(number: Double): Double =
    number.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

fun getShortestPathMatrix(distanceMatrix: Array<Array<Double>>): Array<Array<Double>> =
    floydsAlgorithm(distanceMatrix)

fun getListKilometerWinningMatrix(
    range: IntRange,
    distanceMatrix: Array<Array<Double>>
): MutableList<MutableList<MutableList<Double>>> {
    val listMatrixDistance = mutableListOf<MutableList<MutableList<Double>>>()
    range.forEach {
        val distance = distanceMatrix.map { it.toMutableList() }.toMutableList()
        val cutedMatrix = cutOfConsignee(it, range, distance)
        listMatrixDistance.add(removeNull(cutedMatrix))
    }

    return listMatrixDistance
}

private fun cutOfConsignee(
    item: Int,
    range: IntRange,
    distanceMatrix: MutableList<MutableList<Double>>
): MutableList<MutableList<Double>> {
    var result = distanceMatrix.toMutableList()
    range.forEach {
        if (it != item) {
            result[it].replaceAll { 0.0 }
            val tr = transposeMatrix(result)
            tr[it].replaceAll { 0.0 }
            result = transposeMatrix(tr)
        }
    }
    return result
}
private fun removeNull(listList: MutableList<MutableList<Double>>): MutableList<MutableList<Double>> {
    listList.removeIf { it.sum()==0.0 }
    var r = transposeMatrix(listList)
    r.removeIf { it.sum()==0.0 }
    return transposeMatrix(r)
}
