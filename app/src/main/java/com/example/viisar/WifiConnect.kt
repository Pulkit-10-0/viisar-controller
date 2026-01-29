package com.example.viisar

import android.content.Context
import android.net.*
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import androidx.annotation.RequiresApi
import android.os.Handler
import android.os.Looper
import android.widget.Toast

// 🔑 GLOBAL reference
lateinit var espNetwork: Network

@RequiresApi(Build.VERSION_CODES.Q)
fun connectToWifi(
    context: Context,
    ssid: String,
    password: String,
    onConnected: () -> Unit
) {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val specifier = WifiNetworkSpecifier.Builder()
        .setSsid(ssid)
        .setWpa2Passphrase(password)
        .build()

    val request = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .setNetworkSpecifier(specifier)
        .build()

    cm.requestNetwork(
        request,
        object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                espNetwork = network   // 🔑 STORE NETWORK
                cm.bindProcessToNetwork(network)

                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "Connected to $ssid",
                        Toast.LENGTH_SHORT
                    ).show()
                    onConnected()
                }
            }
        }
    )
}
