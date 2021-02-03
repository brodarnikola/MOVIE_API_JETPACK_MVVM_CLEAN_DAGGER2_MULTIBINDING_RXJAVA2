package com.vjezba.domain.model

import com.google.gson.annotations.SerializedName


data class TrailerResult(
    val id: String? = "",
    @SerializedName("key")
    var key: String = "",

    @SerializedName("genre_ids")
    val genreIds: List<Int> = listOf(),

    val name: String = "",
    val site: String = "",

    val size: Double = 0.0,

    val type: String = "" )