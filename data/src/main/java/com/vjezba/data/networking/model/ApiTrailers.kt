package com.vjezba.data.networking.model


import com.google.gson.annotations.SerializedName
import com.vjezba.domain.model.TrailerResult

data class ApiTrailers(

    @SerializedName("id")
    val id: Long = 0L,
    val results: List<TrailerResult> = listOf()
)
