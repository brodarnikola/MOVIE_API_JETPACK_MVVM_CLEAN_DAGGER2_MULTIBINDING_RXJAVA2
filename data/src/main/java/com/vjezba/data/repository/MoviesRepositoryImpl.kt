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
import com.vjezba.domain.model.Movies
import com.vjezba.domain.repository.MoviesRepository
import io.reactivex.Flowable

/**
 * RepositoryResponseApi module for handling data operations.
 */
class MoviesRepositoryImpl constructor(
    private val dbMovies: MoviesDatabase,
    private val service: MovieRepositoryApi,
    private val dbMapper: DbMapper?
) : MoviesRepository {

    // example, practice of rxjava2
    override fun getMovies(): Flowable<Movies> {
        val moviesResult = service.searchMoviesWithFlowable()

        Log.i("Da li ce uci", "AAAA Hoce li svakih 10 sekundi skinuti nove podatke")
        //Observable.concatArrayEager(newsResult, observableFromDB)

        val correctMoviesResult = moviesResult.map { dbMapper?.mapApiMoviesToDomainMovies(it)!! }

        return correctMoviesResult
    }

    /*override suspend fun getNewsFromLocalDatabaseRoom(): Flowable<List<MovieResult>> {
        val resultRoom = dbMovies.newsDao().getNews().map {
            dbMapper?.mapDBNewsListToNormalNewsList(it) ?: Articles()
        }

        return Flowable.fromArray(resultRoom)
        // Or we could return Flowable.just(News("", "", "", resultRoom)) from domain News..
        // we just need to add this resultRoom to
        // status, source, soryBy will be empty, because we don't need this data
        //return Flowable.just(News("", "", "", resultRoom))
    }
    */

}