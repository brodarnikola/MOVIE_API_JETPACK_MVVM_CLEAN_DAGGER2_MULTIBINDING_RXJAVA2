package com.vjezba.data.networking.model


import com.google.gson.annotations.SerializedName
import com.vjezba.domain.model.MovieResult
import java.util.*

data class ApiMovieDetails(

    val id: Long? = 0,
    val adult: Boolean = false,
    @SerializedName("backdrop_path")
    var backdropPath: String = "",
    val budget: Long = 0L,
    var homepage: String = "",

    @SerializedName("original_language")
    val originalLanguage: String = "",
    @SerializedName("original_title")
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,

    @SerializedName("release_date")
    val releaseDate: Date = Date()

)
