package com.vjezba.data.database.mapper

import com.vjezba.data.database.model.DBMovies
import com.vjezba.data.networking.model.ApiMovieDetails
import com.vjezba.data.networking.model.ApiMovies
import com.vjezba.domain.model.MovieDetails
import com.vjezba.domain.model.MovieResult
import com.vjezba.domain.model.Movies


interface DbMapper {

    fun mapApiMoviesToDomainMovies(apiNews: ApiMovies): Movies

    fun mapApiMovieDetailsToDomainMovieDetails(apiMovieDetails: ApiMovieDetails): MovieDetails

    fun mapDomainMoviesToDbMovies(newsList: Movies): List<DBMovies>

    fun mapDBMoviesListToMovies(articlesList: DBMovies): MovieResult

}