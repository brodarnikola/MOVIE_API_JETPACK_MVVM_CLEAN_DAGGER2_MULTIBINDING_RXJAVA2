package com.vjezba.domain.model

import com.google.gson.annotations.SerializedName


data class ActorsResult(

    val id: Long? = 0L,

    val gender: Int? = 0,

    @SerializedName("known_for_department")
    var knownForDepartment: String = "",

    val name: String = "",
    val character: String = "" )