/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.androidjetpackmovie.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vjezba.domain.model.Actors
import com.vjezba.domain.model.MovieDetails
import com.vjezba.domain.repository.MoviesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class MovieDetailsViewModel @Inject constructor(
    val movieRepository: MoviesRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _newsDetailsMutableLiveData = MutableLiveData<MovieDetails>().apply {
        value = MovieDetails( 0, false, "",  0L, "", "", "", "", 0.0, Date())
    }

    val newsDetailsList: LiveData<MovieDetails> = _newsDetailsMutableLiveData

    fun getMovieDetails(movieId: Long) {

        movieRepository.getMoviesDetails(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorReturn { error ->
                Log.e("onErrorReceived", "OnError return is: ${error}")
                MovieDetails()
            }
            .subscribe(
                this::handleResponse, this::onError
            )
    }

    private fun onError(error: Throwable) {
        Log.e("Error", "Error is: ${error}")
    }

    private fun handleResponse(response: MovieDetails) {
        _newsDetailsMutableLiveData.value?.let {_newsDetailsMutableLiveData.value = response }
    }

    override fun onCleared() {
        super.onCleared()
        if( !compositeDisposable.isDisposed )
            compositeDisposable.dispose()
    }

}
