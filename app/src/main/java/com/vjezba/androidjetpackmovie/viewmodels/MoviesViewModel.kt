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
import com.vjezba.domain.model.MovieResult
import com.vjezba.domain.model.Movies
import com.vjezba.domain.repository.MoviesRepository
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val dbMovies: MoviesDatabase,
    private val dbMapper: DbMapper?,
    private val connectivityUtil: ConnectivityUtil
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _moviesMutableLiveData = MutableLiveData<Movies>().apply {
        value = Movies( 0, listOf(), 0, 0L )
    }

    val moviesList: LiveData<Movies> = _moviesMutableLiveData

    fun getMoviesFromServer() {
        if (connectivityUtil.isConnectedToInternet()) {

//            val list = IntRange(0, 9).toList()
//            val disposable = Observable
//                .interval(0,5, TimeUnit.SECONDS)
//
//                .map { savedLanguages.getNews() }
//                //.map { i -> list[i.toInt()] }
//                .take(list.size.toLong())
//                .subscribe {
//                    it.blockingFirst()
//                    println("item: $it")
//                }

            tryToCreateObservableEvery20Seconds()
            getMoviesFromNetwork()
        }
        else {

            Observable.fromCallable {
                val listDBMovies = getMoviesFromDb()

                Movies(0 , listDBMovies,0,0L)
            }
                .subscribeOn(Schedulers.io())
                //.flatMap { source: List<Articles> -> Observable.fromIterable(source) } // this flatMap is good if you want to iterate, go through list of objects.
                //.flatMap { source: News? -> Observable.fromArray(source) or  } // .. iterate through each item
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { data: Movies? ->

                    _moviesMutableLiveData.value?.let { data ->
                        _moviesMutableLiveData.value = data
                    }
                }
                .subscribe()
        }
    }

    private fun getMoviesFromNetwork() {
        moviesRepository.getMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .subscribe(object : Observer<Movies> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(response: Movies) {

                    insertMoviesIntoDB(response)

                    //_moviesMutableLiveData.value = response

                    _moviesMutableLiveData.value?.let { _moviesMutableLiveData.value = response
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

    private fun tryToCreateObservableEvery20Seconds() {
        //                Observable.interval(
//                    10,
//                    TimeUnit.SECONDS,
//                    Schedulers.io()
//                )
//                    .observeOn(Schedulers.newThread())
//                    .map { tick: Long? -> savedLanguages.getNews() }
//                    .doOnError { error: Throwable ->
//                        Log.e(
//                            "error",
//                            error.toString()
//                        )
//                    }
//                    .doOnSubscribe {
//                        compositeDisposable.add(it)
//                    }
//                    .doOnNext {
//
//                        Log.i(
//                            "Da li ce uci",
//                            "BBBB Hoce li svakih 10 sekundi skinuti nove podatke, unutar viewmodela DATA IS ${it.blockingFirst().result.size}"
//                        )
//                        val test5 = it.blockingFirst()
//
//                        _newsMutableLiveData.value?.let { news ->
//                            _newsMutableLiveData.value = test5
//                        }
//                    }
//                    .retry()
//                    .subscribe {
//                        Log.i(
//                            "Da li ce uci",
//                            "Hoce li svakih 10 sekundi skinuti nove podatke, unutar viewmodela DATA IS ${it.blockingFirst().result.size}"
//                        )
//                        val test5 = it.blockingFirst()
//                        insertNewsIntoDB(test5)
//
//                        it.doOnNext {
//                            _newsMutableLiveData.value?.let { news ->
//                                _newsMutableLiveData.value = it
//                            }
//                        }
//                    }
    }

    private fun getMoviesFromDb(): List<MovieResult> {
        return dbMovies.moviesDAO().getMovies().map {
            dbMapper?.mapDBMoviesListToMovies(it) ?: MovieResult(
                false, "", listOf(), 0L, "", "", "", 0.0

            )
        }
    }

    private fun insertMoviesIntoDB(movies: Movies) {

        Observable.fromCallable {

            dbMovies.moviesDAO().updateMovies(
                dbMapper?.mapDomainMoviesToDbMovies(movies) ?: listOf()
            )
            Log.d(
                "da li ce uci unutra * ",
                "da li ce uci unutra, spremiti podatke u bazu podataka: " + toString() )

        }
            .subscribeOn(Schedulers.io())
            .subscribe {
                Log.d( "Hoce spremiti vijesti","Inserted ${  movies.result.size} movies from API in DB...")
            }
    }

}

