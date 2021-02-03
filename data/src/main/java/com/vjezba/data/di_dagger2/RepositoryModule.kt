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

package com.vjezba.data.di

import com.vjezba.data.database.MoviesDatabase
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.networking.MovieRepositoryApi
import com.vjezba.data.repository.MoviesRepositoryImpl
import com.vjezba.domain.repository.MoviesRepository
import dagger.Module
import dagger.Provides


@Module
class RepositoryModule {

    @Provides
    fun provideAllNewsFromRestApiNetworkOrFromRoom(moviesDatabase: MoviesDatabase, movieRepositoryApi: MovieRepositoryApi, dbMapper : DbMapper) : MoviesRepository {
        return MoviesRepositoryImpl(moviesDatabase, movieRepositoryApi, dbMapper)
    }
}
