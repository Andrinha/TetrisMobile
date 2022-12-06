package com.fit.tetris.utils

import org.joda.time.DateTime

fun Long.toTime(): String {
    val dateTime = DateTime(this)
    val hours = String.format("%02d", dateTime.hourOfDay)
    val minutes = String.format("%02d", dateTime.minuteOfHour)
    return "$hours:$minutes"
}
fun Long.toDate(): String {
    val dateTime = DateTime(this)
    return "${dateTime.dayOfMonth}/${dateTime.monthOfYear}/${dateTime.year}"
}
fun Long.toDateTime(): String {
    return "${this.toDate()} ${this.toTime()}"
}