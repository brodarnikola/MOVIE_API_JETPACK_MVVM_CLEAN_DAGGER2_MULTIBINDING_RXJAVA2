package com.vjezba.androidjetpackmovie.di


import com.vjezba.androidjetpackmovie.ui.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeNewsDetailsFragment(): IntroViewPagerFragment

}
