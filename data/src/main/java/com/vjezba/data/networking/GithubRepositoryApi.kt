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


import com.vjezba.data.networking.model.ApiNews
import io.reactivex.Flowable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Headers


interface GithubRepositoryApi {

    @GET("articles?source=bbc-news&sortBy=top&apiKey=b0124155d6224507b85f340dcbf20df4")
    @Headers("Content-Type: application/json")
    fun searchNewsWithFlowable(
    ): Flowable<ApiNews>


}
