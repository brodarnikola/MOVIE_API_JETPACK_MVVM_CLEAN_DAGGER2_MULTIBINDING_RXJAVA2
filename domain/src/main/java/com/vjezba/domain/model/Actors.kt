package com.vjezba.domain.model

import com.google.gson.annotations.SerializedName


data class Actors(
    @SerializedName("id")
    val id: Long = 0L,
    val cast: List<ActorsResult> = listOf() )