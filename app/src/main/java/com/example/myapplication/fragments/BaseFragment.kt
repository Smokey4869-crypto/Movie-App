package com.example.myapplication.fragments

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myapplication.connectvity.CheckNetworkConnection

open class BaseFragment: Fragment() {
    private lateinit var checkNetworkConnection: CheckNetworkConnection
    var haveInternet: Boolean = true

    protected fun callNetworkConnection() {
        checkNetworkConnection = CheckNetworkConnection(requireActivity().application)
        checkNetworkConnection.observe(this,{ isConnected ->
            if (isConnected){
                haveInternet = true
                Log.v("Conn Check", "Fragment connection available")
            }else{
                haveInternet = false
                Log.v("Conn Check", "Fragment connection unavailable")
            }
        })
    }
}