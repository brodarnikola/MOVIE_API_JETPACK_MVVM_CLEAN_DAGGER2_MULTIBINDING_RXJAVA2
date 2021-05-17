/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.data.repository

import android.util.Log
import com.vjezba.data.database.MoviesDatabase
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.networking.MovieRepositoryApi
import com.vjezba.domain.model.Actors
import com.vjezba.domain.model.MovieDetails
import com.vjezba.domain.model.Movies
import com.vjezba.domain.model.Trailer
import com.vjezba.domain.repository.MoviesRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * RepositoryResponseApi module for handling data operations.
 */
class MoviesRepositoryImpl constructor(
    private val dbMovies: MoviesDatabase,
    private val service: MovieRepositoryApi,
    private val dbMapper: DbMapper?
) : MoviesRepository {

    // example, practice of rxjava2
    override fun getMovies(page: Int): Flowable<Movies> {
        val moviesResult = service.searchMovies(page)

        Log.i("Da li ce uci", "AAAA Hoce li svakih 10 sekundi skinuti nove podatke")
        //Observable.concatArrayEager(newsResult, observableFromDB)

        val correctMoviesResult = moviesResult.map { dbMapper?.mapApiMoviesToDomainMovies(it)!! }

        return correctMoviesResult
    }

    override fun getMoviesDetails( movieId: Long ): Single<MovieDetails> {

        val moviesResult = service.getDetailsMovie(movieId)

        val correctMoviesResult = moviesResult.map { dbMapper?.mapApiMovieDetailsToDomainMovieDetails(it)!! }

        return correctMoviesResult
    }

    override fun getTrailers(movieId: Long): Flowable<Trailer> {

        val moviesResult = service.getTrailers(movieId)

        val correctMoviesResult = moviesResult.map { dbMapper?.mapApiTrailersToDomainTrailers(it)!! }

        return correctMoviesResult
    }

    override fun getActors(movieId: Long): Single<Actors> {

        val moviesResult = service.getActors(movieId)

        val correctMoviesResult = moviesResult.map { dbMapper?.mapApiActorsToDomainActors(it)!! }

        return correctMoviesResult
    }

}
