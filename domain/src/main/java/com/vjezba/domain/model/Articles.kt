package com.vjezba.domain.model


data class Articles(
    val id: Long? = 0,
    val author: String = "",
    val title: String = "",
    val description: String = "",
    val url: String = "",
    val urlToImage: String = "",
    val publishedAt: String? = "" ) {

}