package com.vjezba.data.database.mapper

import com.vjezba.data.database.model.DBNews
import com.vjezba.data.networking.model.ApiNews
import com.vjezba.domain.model.Articles
import com.vjezba.domain.model.News
import io.reactivex.Flowable


interface DbMapper {

    fun mapApiNewsToDomainNews(apiNews: ApiNews): News

    fun mapDomainNewsToDbNews(newsList: News): List<DBNews>

    fun mapDBNewsListToNormalNewsList(articlesList: DBNews): Articles

}