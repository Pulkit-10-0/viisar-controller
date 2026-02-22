package com.example.viisar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview


import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viisar.ui.theme.AccentTeal
import com.example.viisar.ui.theme.DividerDark
import com.example.viisar.ui.theme.SoftOrange
import com.example.viisar.ui.theme.TextPrimary
import com.example.viisar.ui.theme.TextSecondary
import com.example.viisar.ui.theme.VIISARTheme

@Composable
fun GridConfigScreen(
    onCreateMatrix: (rows: Int, cols: Int) -> Unit
) {
    var rowsText by remember { mutableStateOf("5") }
    var colsText by remember { mutableStateOf("5") }

    val rows = rowsText.toIntOrNull() ?: 0
    val cols = colsText.toIntOrNull() ?: 0
    val isValid = rows in 1..20 && cols in 1..20

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        LogoHeader()

        Spacer(modifier = Modifier.height(24.dp))

        Text("Grid Configuration", fontSize = 24.sp)
        Spacer(Modifier.height(8.dp))
        Text("Enter rows and columns (max 20×20)", fontSize = 14.sp)

        Spacer(Modifier.height(24.dp))

        Row {
            OutlinedTextField(
                value = rowsText,
                onValueChange = { rowsText = it },
                label = { Text("Rows", color = TextSecondary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = SoftOrange,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = SoftOrange,
                    focusedLabelColor = SoftOrange,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )

            Spacer(Modifier.width(16.dp))

            OutlinedTextField(
                value = colsText,
                onValueChange = { colsText = it },
                label = { Text("Columns", color = TextSecondary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = SoftOrange,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = SoftOrange,
                    focusedLabelColor = SoftOrange,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            )

        }

        Spacer(Modifier.height(32.dp))

        Button(
            enabled = isValid,
            onClick = {
                onCreateMatrix(rows, cols)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Create Matrix")
        }
    }
}
@Preview(
    name = "Grid Config – LIGHT",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    device = Devices.PIXEL_6
)
@Composable
fun GridConfigScreenPreview() {
    VIISARTheme {
        GridConfigScreen(
            onCreateMatrix = { _, _ -> }
        )
    }
}
