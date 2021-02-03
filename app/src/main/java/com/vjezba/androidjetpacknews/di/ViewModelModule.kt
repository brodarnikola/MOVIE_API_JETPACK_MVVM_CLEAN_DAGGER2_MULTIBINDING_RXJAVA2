package com.vjezba.androidjetpacknews.di

import androidx.lifecycle.ViewModel
import com.vjezba.androidjetpacknews.viewmodels.*

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    // news viewmodels
    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    abstract fun bindNewsActivityiViewModel(viewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsDetailsViewModel::class)
    abstract fun bindNewsDetailsActivityViewModel(viewModel: NewsDetailsViewModel): ViewModel

}
