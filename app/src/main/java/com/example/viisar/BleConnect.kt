package com.example.viisar

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothProfile

@SuppressLint("MissingPermission")
fun connectToBleDevice(
    device: BluetoothDevice,
    onConnected: () -> Unit
) {
    device.connectGatt(
        null,
        false,
        object : BluetoothGattCallback() {

            override fun onConnectionStateChange(
                gatt: BluetoothGatt,
                status: Int,
                newState: Int
            ) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    onConnected()
                }
            }
        }
    )
}
