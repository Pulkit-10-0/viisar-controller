package com.example.viisar.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.viisar.R

@Composable
fun VIISARLogo(
    size: Int = 48
) {
    Image(
        painter = painterResource(id = R.drawable.viisar_logo),
        contentDescription = "VIISAR Logo",
        modifier = Modifier.size(size.dp)
    )
}