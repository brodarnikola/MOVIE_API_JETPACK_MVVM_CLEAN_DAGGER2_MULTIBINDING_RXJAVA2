package com.vjezba.data.networking.model


import com.google.gson.annotations.SerializedName
import com.vjezba.domain.model.ActorsResult

data class ApiActors(

    @SerializedName("id")
    val id: Long = 0L,
    val cast: List<ActorsResult> = listOf()
)
