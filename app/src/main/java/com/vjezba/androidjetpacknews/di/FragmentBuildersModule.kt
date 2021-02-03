package com.vjezba.androidjetpacknews.di


import com.vjezba.androidjetpacknews.ui.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeNewsDetailsFragment(): IntroViewPagerFragment

}
