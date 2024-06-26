package com.example.mapkitresultproject.algorithm

class ClarkeRightAlgorithm(private val dataForClarkeRight: DataForClarkeRight) {

    private var currentKilometerWinnMatrix :MutableList<MutableList<Double>> = mutableListOf()
    private val kilometerWinnMatrix = dataForClarkeRight.matrix
    var cargoVolumes = dataForClarkeRight.cargoVolumes.toMutableList()
    private var carrying = dataForClarkeRight.carrying

    var listRoutes: MutableList<List<Int>> = mutableListOf()
    private var route: MutableList<Int> = mutableListOf()
    private var currentWeight = 0.0

    var rowIndexForRout = -1
    var columnIndexForRout = -1

    var next = true
    var finish = false

    fun start(){
        var checkDeleteWeight = ArrayList<Int>()
        while(cargoVolumes.max()!=0.0){
            kilometerWinnMatrix.forEachIndexed { index, mutableLists ->
                if (findItemWithMaxValue(kilometerWinnMatrix) == index) {
                    finish = false
                    currentKilometerWinnMatrix = mutableLists
                    operation()
                    cargoVolumes.forEachIndexed { index, d ->
                        if (d == 0.0) {
                            checkDeleteWeight.add(index)
                        }
                    }
                }
            }
            checkDeleteWeight.forEach { n ->
                kilometerWinnMatrix.forEach {
                    putNullInMatrix(n)
                }
            }
        }
    }

    fun operation() {
        while (!finish) {
            if (next) findMaxValueInMatrix()

            if (cargoVolumes.size - controlCountNullWeight() <= 2) conditions2()
            else if (kilometerWinnMatrixIsNull()) conditions1()
            else findMaxValuesRowAndColumn()
        }
    }
    private fun findItemWithMaxValue(listItem: MutableList<MutableList<MutableList<Double>>>): Int {
        var list = mutableListOf<Double>()

        listItem.forEachIndexed { index, mutableLists ->
            currentKilometerWinnMatrix = listItem.get(index)
            list.add(returnCheckMaxValues())
        }
        return list.withIndex().maxBy { it.value }.index
    }
    fun controlCountNullWeight(): Int {
        var quantity = 0
        cargoVolumes.forEach {
            if (it == 0.0) {
                quantity++
            }
        }
        return quantity
    }

    fun readRout() {
        //println(resultListForListRout.size)
        for (item in listRoutes) {
            println(item.map { it + 1 })
        }
    }

    fun conditions1() {
        var resultRoute = mutableListOf<Int>()
        cargoVolumes.forEachIndexed() { index, d ->
            if (d != 0.0) {
                resultRoute.add(index)
                cargoVolumes.set(index, 0.0)
            }
        }
        listRoutes.add(resultRoute)
        finish = true
    }

    private fun kilometerWinnMatrixIsNull() = currentKilometerWinnMatrix.all { row -> row.all { number -> number == 0.0 } }
    private fun cargoVolumesIsNull() = cargoVolumes.all { it == 0.0 }
    private fun findMaxValueInMatrix() {
        next = false
        var maxValue = 0.0
        var pair: Pair<Int, Int> = Pair(0, 0)
        for (i in currentKilometerWinnMatrix.indices) {
            for (j in 0 until currentKilometerWinnMatrix[i].size) {
                if (currentKilometerWinnMatrix[i][j] >= maxValue) {
                    maxValue = currentKilometerWinnMatrix[i][j]
                    pair = Pair(i, j)
                }
            }
        }
        fillListRoute(pair.first, pair.second)
        fillCurWeight(pair.first, pair.second)
    }

    private fun fillListRoute(firstPoint: Int, lastPoint: Int) {
        route.add(firstPoint)
        route.add(lastPoint)
    }

    private fun findMaxValuesRowAndColumn() {
        var maxValueRow = 0.0
        var maxValueColumn = 0.0
        for (i in currentKilometerWinnMatrix.indices) {
            if (currentKilometerWinnMatrix[i][route.last()] > maxValueRow && i != route.first()) {
                maxValueRow = currentKilometerWinnMatrix[i][route.last()]
                rowIndexForRout = i
            }
        }
        for (i in currentKilometerWinnMatrix.indices) {
            if (currentKilometerWinnMatrix[route.first()][i] > maxValueColumn && i != route.last()) {
                maxValueColumn = currentKilometerWinnMatrix[route.first()][i]
                columnIndexForRout = i
            }
        }
        addRoutNewPoint(maxValueRow, maxValueColumn)
    }

    private fun addRoutNewPoint(maxValueRow: Double, maxValueColumn: Double) {
        when (checkLimitWeight(maxValueRow, maxValueColumn)) {
            true -> {
                currentWeight += if (maxValueRow > maxValueColumn) {
                    cargoVolumes[route.last()] = 0.0
                    putNullInMatrix(route.last())
                    route.add(rowIndexForRout)
                    cargoVolumes[route.last()]
                } else {
                    cargoVolumes[route.first()] = 0.0
                    putNullInMatrix(route.first())
                    route.add(0, columnIndexForRout)
                    cargoVolumes[route.first()]
                }
            }

            false -> {
                next = true
                cargoVolumes[route.last()] = 0.0
                cargoVolumes[route.first()] = 0.0
                currentWeight = 0.0
                rowIndexForRout = -1
                columnIndexForRout = -1
                putNullInMatrix(route.first())
                putNullInMatrix(route.last())
                listRoutes.add(route.toList())
                route.clear()
                finish = true
                //readMatrix(kilometerWinnMatrix)
                println()
            }
        }
    }

    fun putNullInMatrix(ind: Int) {
        for (i in 0 until currentKilometerWinnMatrix.size) {
            currentKilometerWinnMatrix[i][ind] = 0.0
            currentKilometerWinnMatrix[ind][i] = 0.0
        }
        cargoVolumes[ind] = 0.0
    }

    fun checkLimitWeight(valueRow: Double, valueColumn: Double): Boolean {
        var checkCurWeight = currentWeight
        checkCurWeight += if (valueRow > valueColumn) {
            cargoVolumes[rowIndexForRout]
        } else {
            cargoVolumes[columnIndexForRout]
        }
        return checkCurWeight <= carrying
    }

    private fun fillCurWeight(firstPoint: Int, lastPoint: Int) {
        currentWeight = cargoVolumes[firstPoint] + cargoVolumes[lastPoint]
    }

    fun readMatrix(matrix: Array<Array<Double>>) {
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                print("${matrix[j][i]} \t")
            }
            println()
        }
    }

    fun returnCheckMaxValues(): Double {
        var maxValue = 0.0
        for (i in currentKilometerWinnMatrix.indices) {
            for (j in 0 until currentKilometerWinnMatrix[i].size) {
                if (currentKilometerWinnMatrix[i][j] >= maxValue) {
                    maxValue = currentKilometerWinnMatrix[i][j]
                }
            }
        }
        return maxValue
    }

    fun conditions2() {
        var result = mutableListOf<Int>()
        if (route.size == 3) {
            listRoutes.add(route.toList())
            cargoVolumes.forEachIndexed() { i, _ -> cargoVolumes[i] = 0.0 }
        } else {
            cargoVolumes.forEachIndexed() { index, d ->
                if (d != 0.0) {
                    result.add(index)
                    cargoVolumes.set(index, 0.0)
                }
            }
            listRoutes.add(result)
        }
        finish = true
    }
}