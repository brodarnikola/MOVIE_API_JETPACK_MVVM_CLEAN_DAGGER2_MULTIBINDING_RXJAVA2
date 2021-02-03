package com.vjezba.domain.model


data class News(

    val status: String = "",
    val source: String = "",
    val sortBy: String = "",
    var articles: List<Articles>

)
