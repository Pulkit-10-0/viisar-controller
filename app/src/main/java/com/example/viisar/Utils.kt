package com.example.viisar


import androidx.compose.ui.unit.dp

val GRID_DOT_SIZE = 28.dp
fun Set<Int>.toggle(value: Int): Set<Int> {
    return if (contains(value)) {
        minus(value)
    } else {
        plus(value)
    }
}
fun indexToLabel(index: Int): String {
    if (index < 1) return ""

    val letter = 'A' + (index - 1) % 26
    val repeatCount = (index - 1) / 26 + 1

    return letter.toString().repeat(repeatCount)
}