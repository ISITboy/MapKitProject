package com.example.mapkitresultproject.data.repository

import com.example.mapkitresultproject.data.local.sharedPref.SharedPrefUserStorage
import com.example.mapkitresultproject.domain.repository.SharedPrefUserStorageRepository
import javax.inject.Inject

class SharedPrefUserStorageRepositoryImpl @Inject constructor(
    private val sharedPrefUserStorage: SharedPrefUserStorage
): SharedPrefUserStorageRepository {

    companion object{
        private const val KEY_MAX_VALUE = "uid_value"
    }

    override fun saveUID(uid: String): Boolean {
        sharedPrefUserStorage().edit().putString(KEY_MAX_VALUE,uid).apply()
        return true
    }

    override fun getUID() = sharedPrefUserStorage().getString(KEY_MAX_VALUE,"")?:""

    override fun deleteUID() {
        sharedPrefUserStorage().edit().remove("uid_value").apply()
    }


}