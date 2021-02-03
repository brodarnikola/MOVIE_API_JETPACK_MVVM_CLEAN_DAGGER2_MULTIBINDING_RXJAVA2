package com.vjezba.androidjetpackmovie.di

import androidx.lifecycle.ViewModel
import com.vjezba.androidjetpackmovie.viewmodels.*

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    // news viewmodels
    @Binds
    @IntoMap
    @ViewModelKey(MoviesViewModel::class)
    abstract fun bindNewsActivityiViewModel(viewModel: MoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsDetailsViewModel::class)
    abstract fun bindNewsDetailsActivityViewModel(viewModel: NewsDetailsViewModel): ViewModel

}
