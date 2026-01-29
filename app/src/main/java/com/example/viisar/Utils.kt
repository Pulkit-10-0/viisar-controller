package com.example.viisar

fun Set<Int>.toggle(value: Int): Set<Int> {
    return if (contains(value)) {
        minus(value)
    } else {
        plus(value)
    }
}
