package com.vjezba.domain.model

import com.google.gson.annotations.SerializedName


data class Trailer(
    @SerializedName("id")
    val id: Long = 0L,
    val results: List<TrailerResult> = listOf() )