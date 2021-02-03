package com.vjezba.androidjetpackmovie.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vjezba.androidjetpackmovie.R
import com.vjezba.androidjetpackmovie.di.ViewModelFactory
import com.vjezba.androidjetpackmovie.di.injectViewModel
import com.vjezba.androidjetpackmovie.viewmodels.MovieDetailsViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_news_details.*
import java.text.SimpleDateFormat
import javax.inject.Inject

class MoviesDetailsActivity : AppCompatActivity(), HasActivityInjector, HasSupportFragmentInjector {

    var movieId = 0L

    @Inject
    lateinit var dispatchingAndroidActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidActivityInjector

    @Inject
    lateinit var dispatchingAndroidFragmentInjector:  DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidFragmentInjector

    @Inject lateinit var viewModelFactory: ViewModelFactory
    lateinit var movieDetailsViewModel: MovieDetailsViewModel

    private var dataFetched = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        movieDetailsViewModel = injectViewModel(viewModelFactory)

        this.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        movieId = intent.getLongExtra("movieId", 0L)
    }

    override fun onStart() {
        super.onStart()

        movieDetailsViewModel.newsDetailsList.observe(this, Observer { movie ->
            tvName.text = movie.originalTitle
            tvHomePage.text = "Web site: " + movie.homepage

            Glide.with(this)
                .load( "https://image.tmdb.org/t/p/w500/" + movie.backdropPath)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivMovie)

            tvBudget.text = "Budget: " + movie.budget

            val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy")
            val date: String = simpleDateFormat.format(movie.releaseDate)
            tvReleaseDate.text = "Release date: " + date
        })

        movieDetailsViewModel.getMovieDetails(movieId)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_share, menu)
        return true
    }
    // Helper function for calling a share functionality.
    // Should be used when user presses a share button/menu item.
    @Suppress("DEPRECATION")
    private fun createShareIntent() {
        val shareText = movieDetailsViewModel.newsDetailsList.value.let { data ->
            if (data == null) {
                ""
            } else {
                getString(R.string.share_text_details, data.originalTitle)
            }
        }
        val shareIntent = ShareCompat.IntentBuilder.from(this)
            .setText(shareText)
            .setType("text/plain")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        startActivity(shareIntent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_share -> {
                createShareIntent()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}