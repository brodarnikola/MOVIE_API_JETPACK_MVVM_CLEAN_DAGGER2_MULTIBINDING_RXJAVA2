package com.vjezba.data.database.mapper

import com.vjezba.data.database.model.DBMovies
import com.vjezba.data.networking.model.ApiActors
import com.vjezba.data.networking.model.ApiMovieDetails
import com.vjezba.data.networking.model.ApiMovies
import com.vjezba.data.networking.model.ApiTrailers
import com.vjezba.domain.model.*


interface DbMapper {

    fun mapApiMoviesToDomainMovies(apiNews: ApiMovies): Movies

    fun mapApiMovieDetailsToDomainMovieDetails(apiMovieDetails: ApiMovieDetails): MovieDetails

    fun mapDomainMoviesToDbMovies(newsList: Movies): List<DBMovies>

    fun mapDBMoviesListToMovies(articlesList: DBMovies): MovieResult


    fun mapApiTrailersToDomainTrailers(apiTrailers: ApiTrailers): Trailer

    fun mapApiActorsToDomainActors(apiActors: ApiActors): Actors
}