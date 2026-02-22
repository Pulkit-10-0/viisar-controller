package com.example.viisar

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viisar.ui.theme.VIISARTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
class MainActivity : ComponentActivity() {


    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->

            result.forEach { (permission, granted) ->
                println("$permission = $granted")
            }
        }

    private fun requestPermissions() {

        val permissions = mutableListOf<String>()

        // Location (WiFi + BLE scan)
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        // Android 12+ BLE permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        permissionLauncher.launch(permissions.toTypedArray())
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {



        installSplashScreen()
        super.onCreate(savedInstanceState)

        requestPermissions()   // ✅ CALL HERE ONLY

        setContent {
            VIISARTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val screenStack = remember { mutableStateListOf("splash") }
                    var rows by remember { mutableStateOf(5) }
                    var cols by remember { mutableStateOf(5) }
                    var mode by remember { mutableStateOf<ConnectionMode?>(null) }

                    val screen: String = screenStack.last()


                    BackHandler(enabled = screenStack.size > 1) {
                        screenStack.removeAt(screenStack.lastIndex)
                    }

                    when (screen) {
                        "splash" -> SplashScreen {
                            screenStack.add("connect")
                        }

                        "connect" -> ConnectScreen(
                            onWifiClick = {
                                mode = ConnectionMode.WIFI
                                screenStack.add("wifi")
                            },
                            onBleClick = {
                                mode = ConnectionMode.BLE
                                screenStack.add("ble")
                            }
                        )

                        "wifi" -> WifiScanScreen(
                            context = this,
                            onWifiConnected = {
                                screenStack.add("config")
                            }
                        )

                        "ble" -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                BleScanScreen(
                                    context = this,
                                    onBleConnected = {
                                        screenStack.add("config")
                                    }
                                )
                            }
                        }

                        "config" -> GridConfigScreen { r, c ->
                            rows = r
                            cols = c
                            screenStack.add("grid")
                        }

                        "grid" -> {
                            Scaffold(
                                topBar = {
                                    CenterAlignedTopAppBar(
                                        title = {
                                            Text(
                                                text = "Grid Controller",
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        },
                                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                            containerColor = MaterialTheme.colorScheme.background
                                        )
                                    )
                                }
                            ) { innerPadding ->
                                GridScreen(
                                    rows = rows,
                                    cols = cols,
                                    modifier = Modifier.padding(innerPadding)
                                ) { index, isOn ->

                                    val msg = if (isOn) {
                                        "ON:$index"
                                    } else {
                                        "OFF:$index"
                                    }

                                    when (mode) {
                                        ConnectionMode.WIFI ->
                                            sendMessageToEsp(msg)

                                        ConnectionMode.BLE ->
                                            BleManager.send(msg)

                                        else -> {}

                                    }


                                }
                            }
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun ConnectScreen(
    onWifiClick: () -> Unit,
    onBleClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        LogoHeader()

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Connect Device",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose your connection method",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        ConnectionCard(
            title = "WiFi",
            subtitle = "Connect via wireless network",
            onClick = onWifiClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConnectionCard(
            title = "Bluetooth",
            subtitle = "Connect via Bluetooth",
            onClick = onBleClick
        )
    }
}

@Composable
fun ConnectionCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                subtitle,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}