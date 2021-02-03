package com.vjezba.domain.model

import com.google.gson.annotations.SerializedName


data class MovieResult(
    val adult: Boolean = false,
    @SerializedName("backdrop_path")
    var backdropPath: String = "",
    @SerializedName("genre_ids")
    val genreIds: List<Int> = listOf(),
    val id: Long? = 0,
    @SerializedName("original_language")
    val originalLanguage: String = "",

    @SerializedName("original_title")
    val originalTitle: String = "",

    val overview: String = "",

    val popularity: Double = 0.0,

    var showProgressBar: Boolean = false
    )