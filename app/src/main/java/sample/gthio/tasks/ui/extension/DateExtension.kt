package sample.gthio.tasks.ui.extension

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

fun LocalDate.toDateString(): String {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val tomorrow = today.plus(1, DateTimeUnit.DAY)

    return when (this) {
        today -> "Today"
        tomorrow -> "Tomorrow"
        else -> this.toString()
    }
}

fun LocalTime.toTimeString(): String {
    return "${this.hour}:${this.minute}"
}