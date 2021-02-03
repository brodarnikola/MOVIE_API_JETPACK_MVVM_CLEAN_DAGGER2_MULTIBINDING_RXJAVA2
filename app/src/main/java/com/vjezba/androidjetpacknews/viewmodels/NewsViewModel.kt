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

package com.vjezba.androidjetpacknews.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vjezba.data.database.NewsDatabase
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.networking.ConnectivityUtil
import com.vjezba.data.networking.model.ApiNews
import com.vjezba.data.networking.model.mapToNewsDomain
import com.vjezba.domain.model.Articles
import com.vjezba.domain.model.News
import com.vjezba.domain.repository.NewsRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe.subscribe
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.reactivestreams.Subscriber
import javax.inject.Inject


class NewsViewModel @Inject constructor(
    private val savedLanguages: NewsRepository,
    private val dbNews: NewsDatabase,
    private val dbMapper: DbMapper?,
    private val connectivityUtil: ConnectivityUtil
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _newsMutableLiveData = MutableLiveData<News>().apply {
        value = News("", "", "", listOf())
    }

    val newsList: LiveData<News> = _newsMutableLiveData

    fun getNewsFromServer() {
        if (connectivityUtil.isConnectedToInternet()) {
            savedLanguages.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe(object : io.reactivex.Observer<News> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(response: News) {

                        insertNewsIntoDB(response)

                        _newsMutableLiveData.value?.let { news ->
                            _newsMutableLiveData.value = response
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
        else {

            Observable.fromCallable {
                val listDbArticles = getArticlesFromDb()

                News("", "", "", listDbArticles)
            }
                .subscribeOn(Schedulers.io())
                //.flatMap { source: List<Articles> -> Observable.fromIterable(source) } // this flatMap is good if you want to iterate, go through list of objects.
                //.flatMap { source: News? -> Observable.fromArray(source) or  } // .. iterate through each item
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { newsData: News? ->
                    // perform UI operation on each Item
                    _newsMutableLiveData.value?.let { news ->
                        _newsMutableLiveData.value = newsData
                    }
                }
                .subscribe()
        }
    }

    private fun getArticlesFromDb(): List<Articles> {
        return dbNews.newsDao().getNews().map {
            dbMapper?.mapDBNewsListToNormalNewsList(it) ?: Articles(
                0,
                "",
                "",
                "",
                "",
                "",
                ""
            )
        }
    }

    private fun insertNewsIntoDB(repositoryResult: News) {

        Observable.fromCallable {

            dbNews.newsDao().updateNews(
                dbMapper?.mapDomainNewsToDbNews(repositoryResult) ?: listOf()
                //dbMapper?.mapDomainNewsToDbNews(repositoryResult.blockingFirst()) ?: listOf()
            )
            Log.d(
                "da li ce uci unutra * ",
                "da li ce uci unutra, spremiti podatke u bazu podataka: " + toString() )

        }
            .subscribeOn(Schedulers.io())
            .subscribe {
                Log.d( "Hoce spremiti vijesti","Inserted ${  repositoryResult.articles.size} news from API in DB...")
            }
    }

}
