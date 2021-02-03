package com.vjezba.androidjetpacknews.di

import com.vjezba.data.di.*
import dagger.Module

@Module(includes = [ViewModelModule::class, RepositoryModule::class, DatabaseModule::class, MapperModule::class, NetworkModule::class])
class AppModule {

}
