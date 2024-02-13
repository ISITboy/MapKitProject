package com.example.mapkitresultproject.algorithm

import java.math.RoundingMode

private fun kilometerWinningAlgorithm(distanceMatrix:Array<Array<Double>>):Array<Array<Double>>{
    val currentMatrix: Array<Array<Double>>  =  Array(distanceMatrix.size) { Array(distanceMatrix.size) { 0.0 } }
    for(i in distanceMatrix.indices){
        for (j in 0 until distanceMatrix[i].size){
            val result = distanceMatrix[0][i]+distanceMatrix[0][j]-distanceMatrix[i][j]
            currentMatrix[i][j] = formattingNumber(result)
        }
    }
    return currentMatrix
}

private fun formattingNumber(number: Double) : Double =
    number.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()



private fun putNullsIntoMatrix(distanceMatrix:Array<Array<Double>>):Array<Array<Double>>{
    for(i in distanceMatrix.indices){
        distanceMatrix[i][i]=0.0
    }
    return distanceMatrix
}


fun getKilometerWinningMatrix(distanceMatrix:Array<Array<Double>>): MutableList<MutableList<Double>> = putNullsIntoMatrix(
    kilometerWinningAlgorithm(distanceMatrix)
).map { it.toMutableList() }.toMutableList()



