package com.vjezba.androidjetpackmovie

import android.app.Activity
import android.app.Application
import com.vjezba.androidjetpackmovie.di.AppInjector
import com.vjezba.androidjetpackmovie.network.ConnectivityChangedEvent
import com.vjezba.androidjetpackmovie.network.ConnectivityMonitor
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class App : Application(), HasActivityInjector {

  init {
    ref = this
  }

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  companion object {
    @JvmStatic
    lateinit var ref: App
    //lateinit var instance: Application
    //  private set
  }

  //event bus initialization
  val eventBus: EventBus by lazy {
    EventBus.builder()
      .logNoSubscriberMessages(false)
      .sendNoSubscriberEvent(false)
      .build()
  }

  override fun onCreate() {
    super.onCreate()
    //instance = this

    AppInjector.init(this)

    ConnectivityMonitor.initialize(this) { available ->
      eventBus.post(ConnectivityChangedEvent(available))
    }

  }

  override fun activityInjector() = dispatchingAndroidInjector

}

