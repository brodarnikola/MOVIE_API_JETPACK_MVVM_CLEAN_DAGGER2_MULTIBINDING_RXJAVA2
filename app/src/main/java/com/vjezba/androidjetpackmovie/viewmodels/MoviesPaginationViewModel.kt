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
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MoviesPaginationViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val dbMovies: MoviesDatabase,
    private val dbMapper: DbMapper?,
    private val connectivityUtil: ConnectivityUtil
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _moviesMutableLiveData = MutableLiveData<Movies>().apply {
        value = Movies(0, listOf(), 0, 0L)
    }

    val moviesList: LiveData<Movies> = _moviesMutableLiveData

    private val _page = MutableLiveData<Int>().apply {
        value = 1
    }

    val page: LiveData<Int> = _page

    var pageCounter = 1

    private lateinit var disposable: Disposable

    fun getMoviesFromServer(page: Int) {
        if (connectivityUtil.isConnectedToInternet()) {
            getMoviesFromNetwork(page)
        }
        else {

            Observable.fromCallable {
                val listDBMovies = getMoviesFromDb()

                Movies(0, listDBMovies, 0, 0L)
            }
                .subscribeOn(Schedulers.io())
                //.flatMap { source: List<Articles> -> Observable.fromIterable(source) } // this flatMap is good if you want to iterate, go through list of objects.
                //.flatMap { source: News? -> Observable.fromArray(source) or  } // .. iterate through each item
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { data: Movies? ->
                    Log.d(
                        "da li ce uci unutra * ",
                        "da li ce uci unutra, dohvatiti podatke iz bazu podataka: " + data?.result?.size
                    )
                    _moviesMutableLiveData.value = data
                }
                .subscribe()
        }
    }

    private fun callMoviesRestApi(aLong: Long) {

        val observable: Flowable<Movies> =  moviesRepository.getMovies(pageCounter)
        observable
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            //.map(Function<Movies, Movies> { result: Movies -> result })
            .subscribe(this::handleResults, this::handleError)

//        val test5 = moviesRepository.getMovies(1)
//            .timeInterval(TimeUnit.SECONDS, Schedulers.io())
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .toObservable()
//            .onErrorReturn { error ->
//                Log.e(ContentValues.TAG, "onError received: ${error}")
//                Movies()
//            }
////            .subscribe(
////                this::handleResponse, this::onError
////            )
//            .subscribe(object : Observer<Movies> {
//                override fun onSubscribe(d: Disposable) {
//                    compositeDisposable.add(d)
//                }
//
//                override fun onNext(response: Movies) {
//
//                    insertMoviesIntoDB(response)
//
//                    _moviesMutableLiveData.value?.let {
//                        _moviesMutableLiveData.value = response
//                    }
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.d(
//                        ContentValues.TAG,
//                        "onError received: " + e.message
//                    )
//                }
//
//                override fun onComplete() {
//
//                }
//            })
    }

    private fun handleError(error: Throwable) {
        Log.e("Error", "Error is: ${error}")
    }

    private fun handleResults(response: Movies) {

        insertMoviesIntoDB(response)
        _moviesMutableLiveData.value?.let { _moviesMutableLiveData.value = response }

        Log.d("NewDataReceived", "Will it go here? will new data be arrived? .. COunter is: ${_page.value}, movies response size is: ${response.result.size}")

        if( pageCounter != 1 ) {
            _page.value = _page.value?.plus(1)
        }
        pageCounter++
    }

    private fun getMoviesFromNetwork(page: Int) {
        // TODO: If I would like to fetch data every 20 seconds, then I will need Observale or Flowable
        // If I want just one time to fetch data then is good also this single

//        Observable.interval(5000L, TimeUnit.SECONDS, Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())

        pageCounter = page

        disposable = Observable.interval(
            1000, 10000,
            TimeUnit.MILLISECONDS
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::callMoviesRestApi, this::onError)

    }

    private fun onError(error: Throwable) {
        Log.e("Error", "Error is: ${error}")
    }

//    private fun onError(error: Throwable) {
//        Log.e("Error", "Error is: ${error}")
//    }
//
//    private fun handleResponse(response: Movies) {
//        insertMoviesIntoDB(response)
//        _moviesMutableLiveData.value?.let { _moviesMutableLiveData.value = response }
//    }

    override fun onCleared() {
        super.onCleared()
        if( !compositeDisposable.isDisposed )
            compositeDisposable.dispose()
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

            if( movies != null ) {
                val moviesFinalResult = movies.result.sortedBy { it.id }
                movies.result = moviesFinalResult
                dbMovies.moviesDAO().updateMovies(
                    dbMapper?.mapDomainMoviesToDbMovies(movies) ?: listOf()
                )
                Log.d(
                    "da li ce uci unutra * ",
                    "da li ce uci unutra, spremiti podatke u bazu podataka: " + toString()
                )
            }

        }
            .doOnError { Log.e("Error in observables", "Error is: ${it.message}, ${throw it}") }
            .subscribeOn(Schedulers.io())
            .subscribe {
                Log.d(
                    "Hoce spremiti vijesti",
                    "Inserted ${movies.result.size} movies from API in DB..."
                )
            }
    }

}

