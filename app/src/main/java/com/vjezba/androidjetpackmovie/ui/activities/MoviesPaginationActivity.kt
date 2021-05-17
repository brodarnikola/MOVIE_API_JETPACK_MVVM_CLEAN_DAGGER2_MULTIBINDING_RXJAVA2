package com.vjezba.androidjetpackmovie.ui.activities

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.vjezba.androidjetpackmovie.R
import com.vjezba.androidjetpackmovie.customcontrol.RecyclerViewPaginationListener
import com.vjezba.androidjetpackmovie.di.ViewModelFactory
import com.vjezba.androidjetpackmovie.di.injectViewModel
import com.vjezba.androidjetpackmovie.ui.adapters.MoviesAdapter
import com.vjezba.androidjetpackmovie.viewmodels.MoviesPaginationViewModel
import com.vjezba.data.networking.ConnectivityUtil
import com.vjezba.domain.model.MovieResult
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import kotlinx.android.synthetic.main.activity_movie.*
import javax.inject.Inject


const val pageSizeMoviesScrollToBottomActivity: Int = 20

class MoviesScrollToBottomActivity : BaseActivity(R.id.no_internet_layout), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidActivityInjector

    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var moviesViewModel: MoviesPaginationViewModel

    private lateinit var moviesAdapter: MoviesAdapter
    val moviesLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    private var isLastPage = false
    private var loading = false
    private var page: Int = 1

    @Inject
    lateinit var connectivityUtil: ConnectivityUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies_pagination)

        moviesViewModel = injectViewModel(viewModelFactory)
    }

    override fun onNetworkStateUpdated(available: Boolean) {
        super.onNetworkStateUpdated(available)
        if( viewLoaded == true )
            updateConnectivityUi()
    }

    override fun onStart() {
        super.onStart()
        viewLoaded = true

        initializeUi()

        moviesViewModel.moviesList.observe(this, { news ->
            Log.d(ContentValues.TAG, "Da li ce uci sim uuuuuu: ${news.result.joinToString { "-" }}")
            progressBar.visibility = View.GONE
            if (page > 1)
                moviesAdapter.removeLoading()
            loading = false
            moviesAdapter.updateDevices(news.result.toMutableList())
        })

        moviesViewModel.page.observe(this, { page ->
            // If is not first page, then display snackbar
            if( page != 1 ) {
                val snackbar = Snackbar
                    .make(rootView, "New movies has been downloaded", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        val snackbar1 = Snackbar.make(
                            rootView,
                            "Message is restored!",
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar1.show()
                    }

                snackbar.show()
            }
        })

        moviesViewModel.getMoviesFromServer(page)
    }

    private fun initializeUi() {

        moviesAdapter = MoviesAdapter(mutableListOf<MovieResult>(),
            { movieId: Long -> setMoviesClickListener(movieId) })

        movies_list.apply {
            layoutManager = moviesLayoutManager
            adapter = moviesAdapter
        }
        movies_list.adapter = moviesAdapter


        /**
         * add scroll listener while user reach in bottom load more will call
         */
        movies_list.addOnScrollListener(object :
            RecyclerViewPaginationListener(moviesLayoutManager) {

            override fun loadMoreItems() {
                Log.d("ScrollToBottom", "Will it enter here")
                if (connectivityUtil.isConnectedToInternet()) {
                    loading = true
                    doRestApiCall()
                }
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return loading
            }
        })
    }

    private fun doRestApiCall() {
        moviesAdapter.addLoading()
        page++
        moviesViewModel.getMoviesFromServer(page)

        Log.d(ContentValues.TAG, "Da li ce uci sim uuuuuu pageNumber is: ${page}")
    }

    private fun setMoviesClickListener(movieId: Long) {
        val intent = Intent(this, MoviesDetailsActivity::class.java)
        intent.putExtra("movieId", movieId)
        startActivity(intent)
        finish()
    }

}