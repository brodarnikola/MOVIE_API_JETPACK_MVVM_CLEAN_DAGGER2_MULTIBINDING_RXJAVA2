package com.vjezba.androidjetpacknews.di;

import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory binViewModelFactory(ViewModelFactory factory);
}
