package com.example.viisar

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.util.Log
import androidx.core.app.ActivityCompat
import java.util.UUID

@SuppressLint("MissingPermission")
object BleManager {

    private var gatt: BluetoothGatt? = null
    private var writeChar: BluetoothGattCharacteristic? = null

    private val SERVICE_UUID =
        UUID.fromString("12345678-1234-1234-1234-1234567890ab")
    private val CHAR_UUID =
        UUID.fromString("87654321-4321-4321-4321-ba0987654321")

    @SuppressLint("MissingPermission")
    fun connect(context: Context, device: BluetoothDevice, onReady: () -> Unit) {
        gatt = device.connectGatt(context, false, object : BluetoothGattCallback() {

            override fun onConnectionStateChange(g: BluetoothGatt, s: Int, n: Int) {
                if (n == BluetoothProfile.STATE_CONNECTED) g.discoverServices()
            }

            override fun onServicesDiscovered(g: BluetoothGatt, status: Int) {
                val service = g.getService(SERVICE_UUID) ?: return
                writeChar = service.getCharacteristic(CHAR_UUID)
                if (writeChar != null) onReady()
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun send(msg: String) {
        val c = writeChar ?: return
        c.value = msg.toByteArray()
        c.writeType = BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
        gatt?.writeCharacteristic(c)
    }
}
