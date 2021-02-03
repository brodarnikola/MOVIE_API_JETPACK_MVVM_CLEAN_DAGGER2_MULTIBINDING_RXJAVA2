package com.vjezba.data.networking

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import javax.inject.Inject


class ConnectivityUtil @Inject constructor(private val context: Context) {

    fun isConnectedToInternet(): Boolean {
        var have_WIFI = false
        var have_MobileData = false
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfos = connectivityManager!!.allNetworkInfo
        for (info in networkInfos) {
            if (info.typeName
                    .equals("WIFI", ignoreCase = true)
            ) if (info.isConnected) have_WIFI = true
            if (info.typeName
                    .equals("MOBILE DATA", ignoreCase = true)
            ) if (info.isConnected) have_MobileData = true
        }
        return have_WIFI || have_MobileData
    }
}