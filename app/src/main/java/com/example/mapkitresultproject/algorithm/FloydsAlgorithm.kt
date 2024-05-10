package com.example.mapkitresultproject.algorithm

import com.example.mapkitresultproject.algorithm.utils.transposeMatrix
import java.math.RoundingMode




fun getShortedDistanceMatrix(
    matrix: Array<Array<Double>>,
    countProducer: Int
): MutableList<Array<Array<Double>>> {
    val result = decomposeMatrices(matrix, countProducer)
    result.map { floydsAlgorithm(it) }
    return result
}


fun floydsAlgorithm(distanceMatrix: Array<Array<Double>>): Array<Array<Double>> {
    for (k in distanceMatrix.indices) {
        for (i in distanceMatrix.indices) {
            for (j in distanceMatrix.indices) {
                if ((distanceMatrix[i][j] > distanceMatrix[i][k] + distanceMatrix[k][j]) && distanceMatrix[i][k] < Int.MAX_VALUE && distanceMatrix[k][j] < Int.MAX_VALUE) {
                    distanceMatrix[i][j] =
                        formattingNumber(distanceMatrix[i][k] + distanceMatrix[k][j])
                }
            }
        }
    }
    return distanceMatrix
}

private fun formattingNumber(number: Double): Double =
    number.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()

fun decomposeMatrices(
    matrix: Array<Array<Double>>,
    countProducer: Int
): MutableList<Array<Array<Double>>> {
    val baseSize = matrix.size - (countProducer - 1)
    val resultedMatrix = mutableListOf<Array<Array<Double>>>()
    val incorrectIndex = MutableList(countProducer) { it }
    var indI: Int
    var indJ: Int
    repeat(countProducer) { n ->
        incorrectIndex.removeAt(n)
        indI = 0
        val list = Array(baseSize) { Array(baseSize) { 0.0 } }
        for (i in matrix.indices) {
            indJ = 0
            for (j in 0 until matrix[i].size) {
                if (i !in incorrectIndex && j !in incorrectIndex) {
                    list[indI][indJ] = matrix[i][j]
                    indJ++
                    if (indJ == baseSize) indI++
                }
            }
        }
        resultedMatrix.add(list)
        incorrectIndex.add(n)
        incorrectIndex.sort()
    }
    return resultedMatrix
}
