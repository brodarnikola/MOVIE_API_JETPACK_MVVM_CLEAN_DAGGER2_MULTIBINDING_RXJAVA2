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
import com.vjezba.data.database.MoviesDatabase
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.networking.ConnectivityUtil
import com.vjezba.domain.model.Actors
import com.vjezba.domain.model.MovieResult
import com.vjezba.domain.model.Movies
import com.vjezba.domain.model.Trailer
import com.vjezba.domain.repository.MoviesRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class ActorsViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _actorsMutableLiveData = MutableLiveData<Actors>().apply {
        value = Actors(0, listOf())
    }

    val actorsList: LiveData<Actors> = _actorsMutableLiveData

    fun getActors(movieId: Long) {
        moviesRepository.getActors(movieId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribe(object : Observer<Actors> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(response: Actors) {

                    _actorsMutableLiveData.value?.let {
                        _actorsMutableLiveData.value = response
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d(
                        ContentValues.TAG,
                        "onError received: " + e.message
                    )
                }

                override fun onComplete() {}
            })
    }

    override fun onCleared() {
        super.onCleared()
        if( !compositeDisposable.isDisposed )
            compositeDisposable.dispose()
    }

}

