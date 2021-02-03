package com.vjezba.data.database.mapper

import com.vjezba.data.database.model.DBMovies
import com.vjezba.data.networking.model.ApiMovies
import com.vjezba.domain.model.Articles
import com.vjezba.domain.model.MovieResult
import com.vjezba.domain.model.Movies
import com.vjezba.domain.model.News


interface DbMapper {

    fun mapApiMoviesToDomainMovies(apiNews: ApiMovies): Movies

    fun mapDomainMoviesToDbMovies(newsList: Movies): List<DBMovies>

    fun mapDBMoviesListToMovies(articlesList: DBMovies): MovieResult

}