package com.vjezba.androidjetpackmovie.di


import com.vjezba.androidjetpackmovie.ui.activities.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMoviesPaginationActivity(): MoviesPaginationActivity

    //@ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    @ContributesAndroidInjector
    abstract fun contributeMoviesActivity(): MoviesActivity

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailsActivity(): MoviesDetailsActivity

    @ContributesAndroidInjector
    abstract fun contributeTrailersActivity(): TrailersActivity

    @ContributesAndroidInjector
    abstract fun contributeActorsActivity(): ActorsActivity


}
