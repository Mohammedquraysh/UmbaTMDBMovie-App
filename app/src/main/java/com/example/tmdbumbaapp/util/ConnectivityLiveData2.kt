package com.example.tmdbumbaapp.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData

//this Create custom LiveData to hold boolean value for network connection available or unavailable.
class ConnectivityLiveData2(private val connectivityManager: ConnectivityManager)
    : MutableLiveData<Boolean>() {




    //this help to get Context.CONNECTIVITY_SERVICE right inside the LiveData.
    constructor(application: Application) : this(application.getSystemService(
        Context
            .CONNECTIVITY_SERVICE)
            as ConnectivityManager
    )



    // this Handles the network availability that gets callback when network is available or lost.
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            //4
            postValue(true)
        }



        //When network is unavailable, it updates the value asynchronously to notify its active observers with false value.
        override fun onLost(network: Network) {
            super.onLost(network)
            //5
            postValue(false)
        }
    }


    // When network is available, it updates the value asynchronously to notify its active observers with true value.
    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
        //6
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
    }


    //Registers for network callbacks when there is at least one active observer for the LiveData.
    override fun onInactive() {
        super.onInactive()
        //7


        //  Unregisters for network callbacks when there are no active observers for the LiveData.
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}


