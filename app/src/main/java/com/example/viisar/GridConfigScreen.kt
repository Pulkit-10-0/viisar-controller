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
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = AccentTeal,
                    unfocusedBorderColor = DividerDark,
                    cursorColor = AccentTeal,
                    focusedLabelColor = AccentTeal,
                    unfocusedLabelColor = TextSecondary
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
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = AccentTeal,
                    unfocusedBorderColor = DividerDark,
                    cursorColor = AccentTeal,
                    focusedLabelColor = AccentTeal,
                    unfocusedLabelColor = TextSecondary
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
    name = "Grid Config – Dark",
    showBackground = true,
    backgroundColor = 0xFF0E0F12,
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
