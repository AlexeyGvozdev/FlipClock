package com.sinx.flipclock


val Int.half
    get() = this / 2

fun Int.coerce(): String {
    return if (this > 9) "$this" else "0$this"
}