package com.example.mapkitresultproject.data.remote.dto.distances

import com.google.gson.annotations.SerializedName
import retrofit2.http.Path

data class Query(
    @SerializedName("locations") val locations: List<List<Double>>,
    @SerializedName("metrics") val metrics: List<String>,
    @SerializedName("profile") val profile: String = "driving-car",
    @SerializedName("responseType") val responseType: String? = null
)