package com.example.viisar

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("MissingPermission")
@Composable
fun BleScanScreen(
    context: Context,
    onBleConnected: () -> Unit
) {
    val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
    val scanner = bluetoothAdapter.bluetoothLeScanner

    var devices by remember { mutableStateOf<List<BluetoothDevice>>(emptyList()) }

    DisposableEffect(Unit) {
        val callback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val device = result.device
                if (device.name != null && !devices.contains(device)) {
                    devices = devices + device
                }
            }
        }

        scanner.startScan(callback)

        onDispose {
            scanner.stopScan(callback)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                WindowInsets.statusBars.asPaddingValues()
            )
            .padding(24.dp)
    ) {

        Text(
            text = "Select Bluetooth Device",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        devices.forEach { device ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        BleManager.connect(context, device) {
                            onBleConnected()
                        }
                    }
                ,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(device.name ?: "Unnamed Device")
                    Text(
                        device.address,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
