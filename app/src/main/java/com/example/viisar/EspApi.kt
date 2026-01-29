package com.example.viisar

import java.net.HttpURLConnection
import java.net.URL

fun sendMessageToEsp(msg: String) {
    Thread {
        try {
            val url = URL("http://192.168.4.1/send?msg=$msg")
            (url.openConnection() as HttpURLConnection).apply {
                connectTimeout = 2000
                readTimeout = 2000
                inputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }.start()
}
