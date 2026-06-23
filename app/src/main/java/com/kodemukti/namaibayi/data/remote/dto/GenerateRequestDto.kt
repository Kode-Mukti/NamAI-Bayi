package com.kodemukti.namaibayi.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GenerateRequestDto(
    @SerializedName("baby_name") val babyName: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("preferences") val preferences: List<String> = emptyList(),
    @SerializedName("strategy") val strategy: String? = null,
    @SerializedName("language") val language: String = "id",
)
