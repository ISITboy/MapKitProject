package com.example.mapkitresultproject

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun test(){
        val listMap = listOf("1" to 1,"1" to 1,"2" to 2,"3" to 3)
        var counter = 0
        listMap.forEach {pair ->
            counter += pair.second
        }
        val id = listMap.first { pair -> pair.first == "3" }
        var result:Int? = null
        listMap.forEach {(key,value)->
            if(key == "xx") result = value
        }
        listMap.filter { it.first=="xx" }
        if(result == null) println("not found")

        val map = hashMapOf(null to "2")
        map

        assertEquals(3,listMap.indexOf(id))
    }


    @Test
    fun testFlows() = runBlocking {
        val numbers = 1..10
        val flow : Flow<Int> = numbers.asFlow()
        //холодный поток
        //val flow : Flow<Int> = flowOf(1,2,3,4,5,6,7,7,8,9,10)
        println("11 чисел")
        flow.filter { it%2==0 }.map { it*10 }.collect{
            println(it)
        }

        println("11 чисел")
        flow.filter { it%2==1 }.collect{
            println(it)
        }
    }
}