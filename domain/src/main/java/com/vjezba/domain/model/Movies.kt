package com.vjezba.domain.model

import com.google.gson.annotations.SerializedName


data class Movies(

    @SerializedName("page")
    val page: Int = 0,
    val result: List<MovieResult> = listOf(),
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    @SerializedName("total_results")
    val totalResults: Long = 0L

)
