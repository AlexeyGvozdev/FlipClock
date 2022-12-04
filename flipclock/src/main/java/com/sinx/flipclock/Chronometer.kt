package com.sinx.flipclock

import java.util.Calendar
import java.util.Date
import java.util.Timer
import java.util.TimerTask

internal class Chronometer(
    private val timePeriod: TimePeriod,
    private val timeListener: (start: Int, end: Int) -> Unit
) {

    private val calendar = Calendar.getInstance()
    private val timer = Timer()
    private var isRunning = false

    fun start() {
        if (!isRunning) {
            isRunning = true
            timer.scheduleAtFixedRate(TickTask {
                calendar.time = Date()
                val (start, end) = when (timePeriod) {
                    TimePeriod.MINUTE -> calendar.get(Calendar.HOUR) to calendar.get(Calendar.MINUTE)
                    TimePeriod.SECONDS -> calendar.get(Calendar.MINUTE) to calendar.get(Calendar.SECOND)
                }
                timeListener(start, end)
            }, 0, timePeriod.period)
        }
    }

    fun cancel() {
        isRunning = false
        timer.cancel()
    }
}

private class TickTask(private val tickListener: () -> Unit) : TimerTask() {
    override fun run() {
        tickListener()
    }
}

enum class TimePeriod(val period: Long) {
    MINUTE(60_000),
    SECONDS(1_000);
}