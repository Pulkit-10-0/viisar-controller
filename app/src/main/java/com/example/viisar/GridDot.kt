package com.example.viisar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.viisar.ui.theme.AccentTeal
import com.example.viisar.ui.theme.CardDark
import com.example.viisar.ui.theme.SoftOrange
import com.example.viisar.ui.theme.SurfaceLight
import com.example.viisar.ui.theme.VIISARTheme

@Composable
fun GridDot(
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(GRID_DOT_SIZE)
            .clip(CircleShape)
            .background(
                if (isActive) SoftOrange else SurfaceLight
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isActive) {
            // inner glow / depth effect
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.25f))
            )
        }
    }
}


@Preview(
    name = "Grid Dot – Dark",
    showBackground = true,
    backgroundColor = 0xFF0F1115
)
@Composable
fun GridDotPreview() {
    VIISARTheme {
        Row(
            modifier = Modifier
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GridDot(
                isActive = false,
                onClick = {}
            )

            GridDot(
                isActive = true,
                onClick = {}
            )
        }
    }
}