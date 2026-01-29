package com.example.viisar

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.viisar.ui.theme.VIISARTheme
@Composable
fun GridScreen(
    rows: Int,
    cols: Int,
    modifier: Modifier = Modifier,
    onCellClick: (Int, Boolean) -> Unit
) {
    val activeSet = remember { mutableStateSetOf<Int>() }

    val verticalScroll = rememberScrollState()
    val horizontalScroll = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(verticalScroll)
            .horizontalScroll(horizontalScroll)
            .padding(16.dp)
    ) {
        Column {
            repeat(rows) { r ->
                Row {
                    repeat(cols) { c ->
                        val index = r * cols + c
                        val isOn = index in activeSet

                        GridDot(
                            isActive = isOn,
                            onClick = {
                                val newState = !isOn
                                if (newState) activeSet.add(index)
                                else activeSet.remove(index)

                                onCellClick(index, newState)
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000
)
@Composable
fun GridScreenPreview() {
    VIISARTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Grid Controller") }
                    )
                }
            ) { innerPadding ->
                GridScreen(
                    rows = 20,
                    cols = 6,
                    modifier = Modifier.padding(innerPadding)
                ) { _, _ -> }
            }
        }
    }
}
