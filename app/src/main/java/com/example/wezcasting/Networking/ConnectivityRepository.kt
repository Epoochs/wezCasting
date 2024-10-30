package com.example.myapplication.Networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ConnectivityRepository(context: Context) {
    private lateinit var connectivityManager: ConnectivityManager
    private val isConnected = MutableLiveData(false)

    init {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager!!.registerDefaultNetworkCallback(object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected.postValue(true)
            }

            override fun onLost(network: Network) {
                isConnected.postValue(false)
            }
        })
    }

    fun getIsConnected(): LiveData<Boolean> {
        return isConnected
    }
}