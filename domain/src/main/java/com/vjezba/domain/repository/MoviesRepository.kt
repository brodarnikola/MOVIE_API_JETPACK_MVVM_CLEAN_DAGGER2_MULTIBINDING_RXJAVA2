/*
 * Copyright 2020 Google LLC
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

package com.vjezba.domain.repository

import com.vjezba.domain.model.Actors
import com.vjezba.domain.model.MovieDetails
import com.vjezba.domain.model.Movies
import com.vjezba.domain.model.Trailer
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single


interface MoviesRepository {

    fun getMovies(page: Int) : Flowable<Movies>

    fun getMoviesDetails(movieId: Long) : Single<MovieDetails>

    fun getTrailers(movieId: Long) : Flowable<Trailer>

    fun getActors(movieId: Long) : Single<Actors>
}
