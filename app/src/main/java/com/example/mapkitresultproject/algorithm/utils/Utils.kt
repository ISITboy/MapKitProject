package com.example.mapkitresultproject.algorithm.utils

fun transposeMatrix(matrix: MutableList<MutableList<Double>>): MutableList<MutableList<Double>> {
    val rows = matrix.size
    val cols = matrix[0].size

    val transposedMatrix = MutableList(cols) { MutableList(rows) { 0.0 } }

    for (i in 0 until rows) {
        for (j in 0 until cols) {
            transposedMatrix[j][i] = matrix[i][j]
        }
    }

    return transposedMatrix
}