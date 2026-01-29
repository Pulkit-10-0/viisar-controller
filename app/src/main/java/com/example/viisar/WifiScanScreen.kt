package com.example.viisar

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@RequiresApi(Build.VERSION_CODES.Q)
@SuppressLint("MissingPermission")
@Composable
fun WifiScanScreen(
    context: Context,
    onWifiConnected: () -> Unit
) {
    val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    val networks = wifiManager.scanResults
        .map { it.SSID }
        .filter { it.isNotBlank() }
        .distinct()

    var selectedSsid by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                WindowInsets
                    .statusBars
                    .asPaddingValues()
            )
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {

        Text(
            text = "Select WiFi Network",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        networks.forEach { ssid ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { selectedSsid = ssid }
            ) {
                Text(ssid, modifier = Modifier.padding(16.dp))
            }
        }
    }

    selectedSsid?.let { ssid ->
        WifiPasswordDialog(
            ssid = ssid,
            onConnect = { password ->
                connectToWifi(
                    context = context,
                    ssid = ssid,
                    password = password,
                    onConnected = onWifiConnected   // 🔑 PASS UP
                )
                selectedSsid = null
            },
            onDismiss = { selectedSsid = null }
        )
    }
}
@Composable
fun WifiPasswordDialog(
    ssid: String,
    onConnect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var password by remember { mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Connect to $ssid") },
        text = {
            Column {
                Text("Enter WiFi password")
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Text(
                "Connect",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onConnect(password)
                    }
            )
        },
        dismissButton = {
            Text(
                "Cancel",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onDismiss() }
            )
        }
    )
}
