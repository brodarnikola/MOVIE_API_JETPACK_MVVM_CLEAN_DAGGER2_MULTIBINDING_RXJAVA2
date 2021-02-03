package com.vjezba.data.networking.model


import com.google.gson.annotations.SerializedName
import com.vjezba.domain.model.Articles
import com.vjezba.domain.model.News

data class ApiNews(

    @SerializedName("status")
    val status: String = "",
    val source: String = "",
    val sortBy: String = "",
    val articles: List<Articles>
)


fun ApiNews.mapToNewsDomain(): News {
    return News(status, source, sortBy, articles)
}