package com.example.mapkitresultproject.algorithm.newAlgo

import com.example.mapkitresultproject.algorithm.newAlgo.models.Transport
import kotlin.math.roundToInt

fun printMatrix(matrix : Array<Array<Double>>){
    for (row in matrix){
        for(column in row){
            print("$column \t")
        }
        println()
    }
}

fun distributeNeeds(needs : MutableList<Double>, transports:List<Transport>, listMatrixDistance: List<Array<Array<Double>>>){
    val routes = mutableListOf<Pair<Int, List<Int>>>()
    val listLifting = transports.map { it.lifting }
    //var index =0
    var n =0
    for(i in 0 until needs.size){
        listLifting.forEachIndexed { index, d ->
            if (needs[i] != d){
                n = (needs[i] / listLifting[index]).toInt()
                if(n>=1){
                    needs[i] = ((needs[i]-listLifting[index]) * 100).roundToInt() / 100.0
                }
                repeat(n){
                    routes.add(Pair(findMinDistanceBetweenGO(i, listMatrixDistance), listOf(i)))
                println(transports[index].name)
                println(Pair(findMinDistanceBetweenGO(i, listMatrixDistance), listOf(i)))
                println(needs)
                }
            }
        }
    }
    println(routes)
}

private fun findMinDistanceBetweenGO(i :Int,listMatrixDistance: List<Array<Array<Double>>>): Int {
    val pairs = mutableListOf<Pair<Int,Double>>()
    listMatrixDistance.forEachIndexed { index, matrixDistance ->
        pairs.add(Pair(index,matrixDistance[0][i]))
    }
    return pairs.minBy { it.second }.first
}

