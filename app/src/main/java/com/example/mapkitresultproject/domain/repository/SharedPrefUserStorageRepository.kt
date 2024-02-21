package com.example.mapkitresultproject.domain.repository

interface SharedPrefUserStorageRepository {
    fun saveUID(uid:String):Boolean
    fun getUID():String
    fun deleteUID()
}