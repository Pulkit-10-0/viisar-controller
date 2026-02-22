    package com.example.viisar

    import androidx.activity.compose.BackHandler
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.horizontalScroll
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.LazyRow
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material3.Button
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
        activeSet: MutableSet<Int> = remember { mutableStateSetOf<Int>() },
        onCellClick: (Int, Boolean) -> Unit
    ) {

        val verticalScroll = rememberScrollState()
        val horizontalScroll = rememberScrollState()

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔥 SCROLLABLE GRID AREA
            Box(
                modifier = Modifier
                    .weight(1f) // keeps grid centered vertically
                    .verticalScroll(verticalScroll)
                    .horizontalScroll(horizontalScroll),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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

                                Spacer(Modifier.width(8.dp))
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))


            Button(
                onClick = {
                    activeSet.clear()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Reset Grid")
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
                        rows =20,
                        cols = 6,
                        modifier = Modifier.padding(innerPadding)
                    ) { _, _ -> }
                }
            }
        }
    }


    @Preview(name = "Grid Selected State", showBackground = true, backgroundColor = 0xFF000000)
    @Composable
    fun GridScreenSelectedPreview() {
        VIISARTheme {

            val state = remember {
                mutableStateSetOf(
                    0, 1, 2,
                    7, 8,
                    15
                )
            }

            GridScreen(
                rows = 6,
                cols = 6,
                activeSet = state
            ) { _, _ -> }
        }
    }


    @Preview(name = "After Reset", showBackground = true, backgroundColor = 0xFF000000)
    @Composable
    fun GridScreenResetPreview() {
        VIISARTheme {
            val state = remember { mutableStateSetOf<Int>() } // empty

            GridScreen(
                rows = 6,
                cols = 6,
                activeSet = state
            ) { _, _ -> }
        }
    }