package com.vjezba.androidjetpacknews.di


import com.vjezba.androidjetpacknews.ui.activities.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeNewsActivity(): NewsActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeNewsDetailsActivity(): NewsDetailsActivity

}
