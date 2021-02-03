package com.vjezba.androidjetpackmovie.ui.activities

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.vjezba.androidjetpackmovie.App
import com.vjezba.androidjetpackmovie.network.ConnectivityChangedEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

open class BaseActivity(noWifiViewId: Int = 0) : AppCompatActivity() {

    protected var viewLoaded = false

    protected var networkAvailable: Boolean = true
        private set

    private val uiHandler by lazy { Handler(Looper.getMainLooper()) }

    private val noWifiFrame by lazy { if (noWifiViewId != 0) findViewById<FrameLayout>(noWifiViewId) else null }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkStateChangedEvent(connectivityChangedEvent: ConnectivityChangedEvent) {
        networkAvailable = connectivityChangedEvent.networkAvailable
        onNetworkStateUpdated(connectivityChangedEvent.networkAvailable)
    }

    open fun onNetworkStateUpdated(available: Boolean) {}

    override fun onResume() {
        super.onResume()
        App.ref.eventBus.register(this)
    }

    override fun onPause() {
        super.onPause()
        App.ref.eventBus.unregister(this)
    }

    fun updateConnectivityUi() {
        if (noWifiFrame != null ) {
            noWifiFrame?.visibility = if (networkAvailable) View.GONE else View.VISIBLE
        }
    }
}