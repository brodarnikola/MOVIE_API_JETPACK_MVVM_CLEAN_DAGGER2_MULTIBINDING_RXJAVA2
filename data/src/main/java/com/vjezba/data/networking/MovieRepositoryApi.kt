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

package com.vjezba.data.networking


import com.vjezba.data.networking.model.ApiActors
import com.vjezba.data.networking.model.ApiMovies
import com.vjezba.data.networking.model.ApiMovieDetails
import com.vjezba.data.networking.model.ApiTrailers
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieRepositoryApi {

    @GET("discover/movie?api_key=fea6a69ff7391818240b67fa3bb83786&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page")
    @Headers("Content-Type: application/json")
    fun searchMovies( @Query("page") page: Int ): Single<ApiMovies>

    @GET("movie/{movieId}?api_key=fea6a69ff7391818240b67fa3bb83786&language=en-US")
    @Headers("Content-Type: application/json")
    fun getDetailsMovie( @Path("movieId") movieId: Long ): Flowable<ApiMovieDetails>

    @GET("movie/{movieId}/videos?api_key=fea6a69ff7391818240b67fa3bb83786&language=en-US")
    @Headers("Content-Type: application/json")
    fun getTrailers( @Path("movieId") movieId: Long ): Flowable<ApiTrailers>


    @GET("movie/{movieId}/credits?api_key=fea6a69ff7391818240b67fa3bb83786&language=en-US")
    @Headers("Content-Type: application/json")
    fun getActors( @Path("movieId") movieId: Long ): Flowable<ApiActors>

}
