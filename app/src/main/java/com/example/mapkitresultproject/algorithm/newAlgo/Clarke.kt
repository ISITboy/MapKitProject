package com.example.mapkitresultproject.algorithm.newAlgo

import com.example.mapkitresultproject.algorithm.newAlgo.models.IncorrectTonnageValue
import com.example.mapkitresultproject.algorithm.newAlgo.models.MaxValueData

class Clarke(private val listMatrixKmWin: List<Array<Array<Double>>>, private val needs: MutableList<Double>) {

    private lateinit var currentMatrixKmWin: Array<Array<Double>>
    private var currentIndexMatrixKmWin: Int = 0

    private var maxTonnage = 0.0
    fun setMaxTonnage(tonnage: Double) {
        if (tonnage > 0) maxTonnage = tonnage
        else throw Exception("set max tonnage is incorrect")
    }

    fun getMaxTonnage() = maxTonnage

    private var currentTonnage = 0.0
    private val incorrectTonnageValues = mutableListOf<IncorrectTonnageValue>()
    private val currentRoute = mutableListOf<Int>()
    private val routes = mutableListOf<Pair<Int, List<Int>>>()
    fun getRoutes() = routes

    private var rowIndexForRout = -1
    private var columnIndexForRout = -1

    private var running = true


    fun startMethod() {
        try {
            runningMethod()
        } catch (exception: IndexOutOfBoundsException) {
            println("finish method with ${exception.message}")
            when (needs.count { it != 0.0 }) {
                1 -> routes.add(Pair(currentIndexMatrixKmWin, listOf(needs.indexOfFirst { it != 0.0 })))
                else -> formationOfFinalRoutes()
            }
        }
    }

    private fun runningMethod() {
        while (true) {
            var maxValue: MaxValueData
            do {
                maxValue = getMaxValue()
            } while (!currentTonnageIsCorrect(maxValue))

            println(maxValue)
            printMatrix(currentMatrixKmWin)
            currentRoute.add(maxValue.firstPoint)
            currentRoute.add(maxValue.lastPoint)
            currentTonnage = needs[currentRoute.first()] + needs[currentRoute.last()]
            running = true
            while (running) findMaxValuesRowAndColumn()
        }
    }

    private fun getMaxValue(): MaxValueData {
        val values = mutableListOf<MaxValueData>()
        var value: MaxValueData
        listMatrixKmWin.forEachIndexed { index, matrixKmWin ->
            value = findMaxValueInMatrixKmWin(
                matrixKmWin,
                    incorrectTonnageValues.filter { it.indexMatrix == index }.map { it.pair }
            )
            values.add(value.copy(indexMatrix = index))
        }
        val maxValue = values.maxBy { it.maxValue }
        currentMatrixKmWin = listMatrixKmWin[maxValue.indexMatrix]
        currentIndexMatrixKmWin = maxValue.indexMatrix
        return maxValue
    }

    private fun findMaxValueInMatrixKmWin(
        matrixKmWin: Array<Array<Double>>,
        incorrectTonnageValues: List<Pair<Int, Int>>
    ): MaxValueData {
        var maxValue = 0.0
        var firstPoint = 0
        var lastPoint = 0
        for (i in 0 until matrixKmWin.size) {
            for (j in 0 until matrixKmWin[i].size) {
                if (matrixKmWin[i][j] >= maxValue && !incorrectTonnageValues.contains(Pair(i, j))) {
                    maxValue = matrixKmWin[i][j]
                    firstPoint = i
                    lastPoint = j
                }
            }
        }
        println("i;j -> $firstPoint;$lastPoint")
        return MaxValueData(maxValue = maxValue, firstPoint = firstPoint, lastPoint = lastPoint)
    }

    private fun currentTonnageIsCorrect(maxValueData: MaxValueData): Boolean {
        return if (needs[maxValueData.firstPoint] + needs[maxValueData.lastPoint] <= maxTonnage) true
        else {
            incorrectTonnageValues.add(
                IncorrectTonnageValue(
                    Pair(maxValueData.firstPoint, maxValueData.lastPoint),
                    maxValueData.indexMatrix
                )
            )
            false
        }
    }


    private fun findMaxValuesRowAndColumn() {
        println("___")
        var maxValueRow = 0.0
        var maxValueColumn = 0.0
        for (i in currentMatrixKmWin.indices) {
            if (currentMatrixKmWin[i][currentRoute.last()] > maxValueRow && i != currentRoute.first()
                && !getIncorrectTonnageListForCurrentMatrixKmWin(i) { it.pair.second }) {
                maxValueRow = currentMatrixKmWin[i][currentRoute.last()]
                rowIndexForRout = i
            }
            if (currentMatrixKmWin[currentRoute.first()][i] > maxValueColumn && i != currentRoute.last()
                && !getIncorrectTonnageListForCurrentMatrixKmWin(i) { it.pair.first }) {
                maxValueColumn = currentMatrixKmWin[currentRoute.first()][i]
                columnIndexForRout = i
            }
        }
        println("idRow=$rowIndexForRout idColumn=$columnIndexForRout")
        println("maxRow=$maxValueRow maxColumn=$maxValueColumn")
        println("=================")
        printMatrix(currentMatrixKmWin)
        println("=================")
        addNewPointToRoute(maxValueRow, maxValueColumn)
    }

    private fun getIncorrectTonnageListForCurrentMatrixKmWin(i: Int, predicate: (IncorrectTonnageValue) -> Int)
    = incorrectTonnageValues.any { it.indexMatrix == currentIndexMatrixKmWin && predicate(it) == i }



    private fun addNewPointToRoute(valueRow: Double, valueColumn: Double) {
        when (checkLimitWeight(valueRow, valueColumn)) {
            true -> {
                if (valueRow > valueColumn) {
                    putNull(currentRoute.last())
                    currentRoute.add(rowIndexForRout)
                    currentTonnage += needs[currentRoute.last()]
                } else {
                    putNull(currentRoute.first())
                    currentRoute.add(0, columnIndexForRout)
                    currentTonnage += needs[currentRoute.first()]
                }
            }

            false -> {
                running = false
                println("currentTonnage $currentTonnage")
                currentTonnage = 0.0
                rowIndexForRout = -1
                columnIndexForRout = -1
                putNull(currentRoute.first())
                putNull(currentRoute.last())
                println("currentRoute $currentRoute")
                println("needs $needs")
                routes.add(Pair(currentIndexMatrixKmWin, currentRoute.toList()))
//                addRoutInResultListForListRout(listRout)
                currentRoute.clear()
                printMatrix(currentMatrixKmWin)
            }
        }
    }

    private fun putNull(index: Int) {
        listMatrixKmWin.forEach { matrixKmWin ->
            for (i in matrixKmWin.indices) {
                matrixKmWin[i][index] = 0.0
                matrixKmWin[index][i] = 0.0
            }
        }
        needs[index] = 0.0
    }

    private fun checkLimitWeight(valueRow: Double, valueColumn: Double): Boolean {
        var checkCurrentTonnage = currentTonnage

        checkCurrentTonnage += if (valueRow > valueColumn) {
            needs[rowIndexForRout]
        } else {
            needs[columnIndexForRout]
        }

        return if (valueRow == 0.0 && valueColumn == 0.0) false       //избежать попадания одинаковых пунктов
        else checkCurrentTonnage <= maxTonnage
    }

    private fun formationOfFinalRoutes() {
        currentTonnage = 0.0
        currentRoute.clear()
        val pairs = mutableListOf<Pair<Int, Double>>()
        needs.forEachIndexed { index, d ->
            if (d != 0.0) pairs.add(Pair(index, d))
        }

        pairs.sortedBy { it.second }.forEach { pair ->
            if (currentTonnage + pair.second <= maxTonnage) {
                currentRoute.add(pair.first)
                needs[pair.first] = 0.0
                currentTonnage += pair.second
            } else {
                if (currentRoute.isEmpty()) {
                    currentRoute.add(pair.first)
                    needs[pair.first] = 0.0
                    routes.add(Pair(currentIndexMatrixKmWin, currentRoute.toList()))
                    currentRoute.clear()
                } else {
                    routes.add(Pair(currentIndexMatrixKmWin, currentRoute.toList()))
                    currentRoute.clear()
                    currentTonnage = 0.0
                    currentRoute.add(pair.first)
                    needs[pair.first] = 0.0
                    currentTonnage += pair.second
                }
            }
        }
        routes.add(Pair(currentIndexMatrixKmWin, currentRoute.toList()))
    }

    companion object {

    }
}