package com.example.mapkitresultproject.data.repository

import android.util.Log
import com.example.mapkitresultproject.data.remote.FirebaseService
import com.example.mapkitresultproject.data.remote.Reference
import com.example.mapkitresultproject.domain.models.User
import com.example.mapkitresultproject.domain.state.RealtimeCRUDState
import com.example.mapkitresultproject.domain.state.SearchState
import com.example.mapkitresultproject.domain.usecase.sharedpreferences.GetUIDUseCase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseServiceImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val getUIDUseCase: GetUIDUseCase
) : FirebaseService {

    private val references = firebaseDatabase.getReference(Reference.USER.value)
    private val state = MutableStateFlow<RealtimeCRUDState>(RealtimeCRUDState.Off)

    override fun getState() = state

    override suspend fun createUser(user: User.Base) {
        val userReference = references.child(user.id)
        userReference.setValue(user)
            .addOnSuccessListener {
                state.value = RealtimeCRUDState.Success("Create User")
            }
            .addOnFailureListener {
                state.value = RealtimeCRUDState.Error(it.message.toString())
            }

    }

    override suspend fun updateUser(name:String,organization:String) {
        val userReference = references.child(getUIDUseCase())
        val updates = hashMapOf<String, Any>(
            "name" to name,
            "organization" to organization
        )
        userReference.updateChildren(updates)
            .addOnSuccessListener {
                state.value = RealtimeCRUDState.Success("Update User")
            }
            .addOnFailureListener { error ->
                state.value = RealtimeCRUDState.Error(error.message.toString())
            }

    }

    override suspend fun readUser() {
        val userReference = references.child(getUIDUseCase())
        userReference.get()
            .addOnSuccessListener {
                val retrievedUser = it.getValue(User.Base::class.java)
                retrievedUser?.let {
                    state.value = RealtimeCRUDState.Success(user = retrievedUser)
                }
            }
            .addOnFailureListener {
                state.value = RealtimeCRUDState.Error(it.message.toString())
            }
    }

}